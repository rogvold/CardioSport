function gup( name ){
    name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");  
    var regexS = "[\\?&]"+name+"=([^&#]*)";  
    var regex = new RegExp( regexS );  
    var results = regex.exec( window.location.href ); 
    if( results == null )    return "";  
    else    return results[1];
}


function initActivitiesListHTML(divId){
    $( divId).children('.scroll').nanoScroller();
    //                $('#support-tickets .support-msg').append('<span class="support-full">VIEW FULL TICKET</span>');
    $(divId + ' li .info_button').bind('click', function() {
        //        alert('info');
        var ts = $(this).parent();
        var supMsgHeight = ts.children('.support-msg').height() + 220;
        contHeight = $(divId).outerHeight();
        liPosTop = ts.position().top;
        if ( ts.hasClass('expanded') ) {
            ts.removeClass('expanded').removeAttr('style');
        } else {
            ts.addClass('expanded').css('height', supMsgHeight)
            .siblings('li.expanded').removeClass('expanded').removeAttr('style');
        };
        if ( liPosTop + supMsgHeight > contHeight ) {
            ts.parents('.scroll-cont').animate({
                scrollTop: supMsgHeight
            }, 600);
        } else if ( ts.is(':nth-last-child(-n+3)') ) {
            ts.parents('.scroll-cont').animate({
                scrollTop: contHeight + 41
            }, 600);
        };
    }).children('p').click( function(e) {
        return false;
    });
}

function createActivityManual(data){
    Man = {};
    for (var i in data){
        Man[data[i].id] = {
            name: data[i].name,
            description: data[i].description,
            minHeartRate: data[i].minHeartRate,
            maxHeartRate: data[i].maxHeartRate,
            minSpeed: data[i].minSpeed,
            maxSpeed: data[i].maxSpeed,
            minTension: data[i].minTension,
            maxTension: data[i].maxTension,
            duration: data[i].duration
        };
    }
}

function loadManual(){
    $.ajax({
        url: "/CardioSportWeb/resources/activity/all_activities_list",
        type: "POST",
        success: function(data){
            d = data;
            createActivityManual(data.data);
        },
        error: function(){
            console.log('loadManual: ajax error...');
        }
    });
}


function createWorkout(){
    var name = $('#workout_name_input').val();
    var description = $('#workout_description_input').val();
    var data = {
        'activities': pool, 
        'name' : name , 
        'description' : description
    };
    dr = data;
    console.log('createWorkout: data = ' + data);
    //    var d = "name="+name+"&description="+description+"&list="+pool;
    //    $.post('/CardioSportWeb/resources/workout/create_workout', data);
    
    $.ajax({
        url: "/CardioSportWeb/resources/workout/create_workout",
        data: {
            json: JSON.stringify(data)
        },
        type: "POST",
        success: function(data){
            dd = data;
            window.location.reload();
        },
        error: function(){
        //            registrationFailedCallback("Проверьте подключение к интернету");
        }
    });
}

function generateActivityInfo(actId){
    var activity = Man[actId];
    return '<li> <span class="support-name">' + activity.name + '<b><span class="info_dur">' + activity.duration + '</span></b> мин.</span><button class="blue info_button">инфо</button> <p class="support-msg">' + activity.description + ' <table class="g16" style="margin-left: 10px;"> <tbody> <tr> <td style="width: 240px;">Минимальный пульс</td> <td class="orange-text">'+activity.minHeartRate+'</td> </tr> <tr> <td>Максимальный пульс</td> <td class="orange-text">'+activity.maxHeartRate+'</td> </tr> <tr> <td>Минимальная скорость</td> <td class="orange-text">'+activity.minSpeed+' км/ч</td> </tr> <tr> <td>Максимальная скорость</td> <td class="orange-text">'+activity.maxSpeed+' км/ч</td> </tr> <tr> <td>Минимальное напряжение</td> <td class="orange-text">'+activity.minTension+'</td> </tr> <tr> <td>Максимальное напряжение</td> <td class="orange-text">'+activity.maxTension+'</td> </tr> </tbody> </table> </p> </li>';
}

function generateActivitiesInfoList(list, name, description){
    name = (name == undefined) ? '' : ('<h1 style="padding:5px;">' +name + '</h1>');
    description = (description == undefined) ? '' : ('<br/><span class="orange-text" style="padding:5px;" >' + description + '</span>');
    var preHtml = name + description +  ' <div class="scroll has-scrollbar" style="height: 270px;" > <ul class="ul-grad scroll-cont" tabindex="0" style="right: -15px;">';
    var postHtml = '</ul> <div class="pane" style="display: block;"><div class="slider" style="height: 143px; top: 22px;"></div></div></div> ';
    var html = '';
    for (var i in list){
        console.log('adding html for actId = ' + list[i]);
        html+=generateActivityInfo(list[i]);
    //        console.log(html);
    }
    return preHtml + html + postHtml;
}

function initVariables(){
    pool = [];
    loadManual();
    initActivitiesListHTML('#selected-activities');
    $('.add_button').bind('click', function(){
        var actId = $(this).attr('data-id');
        addActivity(actId);
        console.log(pool);
        //        console.log(getPoolHtml());
        $('#selected-activities').html(getPoolHtml());
        initActivitiesListHTML('#selected-activities');
    });
    
    $('#workout_name_input').keyup(function() {
        if((pool.length > 0) && ($('#workout_name_input').val() !="")){
            $('#create_button').show();
        }else{
            $('#create_button').hide();

        }
    });
    
    $('.info_workout_button').live('click', function(){
        var wId = $(this).attr('data-id');
        drawWorkoutInfo(wId);
    });
    
}

function addActivity(actId){
    //    alert('add:'+actId);
    pool.push(actId);
    $('#pop_button').show();
    
    if((pool.length > 0) && ($('#workout_name_input').val() !="")){
        $('#create_button').show();
    }else{
        $('#create_button').hide();

    }
}

function eraseLast(list){
    var l = new Array();
    for (var i in list){
        if (i == list.length - 1){
            break;
        }
        l.push(list[i]);
    }
    return l;
}

function popActivity(){
    if (pool.length < 1){
        return;
    }
    pool = eraseLast(pool);
    $('#selected-activities').html(getPoolHtml());
    initActivitiesListHTML('#selected-activities');

    if (pool.length == 0){
        $('#pop_button').hide();
    }
    
    if((pool.length > 0) && ($('#workout_name_input').val() !="")){
        $('#create_button').show();
    }else{
        $('#create_button').hide();

    }
}

function getPoolHtml(){
    return generateActivitiesInfoList(pool);
}

function drawWorkoutInfo(workoutId){
    $.ajax({
        url: "/CardioSportWeb/resources/workout/info",
        data: {
            id : workoutId  
        },
        type: "POST",
        success: function(data){
            de = data;
            var arr = new Array();
            for (var i in data.data.activities) arr.push(data.data.activities[i].id);
            var h = generateActivitiesInfoList(arr, data.data.name, data.data.description);
            $('#workout_info_accordion').html(h);
            initActivitiesListHTML('#workout_info_accordion');
        }
    });
}

function getAllWorkouts(){
    $.ajax({
        url: "/CardioSportWeb/resources/workout/get_all",
        type: "POST",
        success: function(data){
            de = data;
        }
    });
}

function drawAllRRPlots(workoutId){
    $.ajax({
        url: "/CardioSportWeb/resources/workout/activity_sessions",
        data: {
            workoutId : workoutId
        },
        type: "POST",
        success: function(data){
            de = data;
            console.log(de);
            drawAllActivityPlots(data.data);
        }
    });
}

function initRRPlots(){
    drawAllRRPlots(getParameter("workoutId"));
}


function filterDisconnectionPoints(points){
    var DISCONNECTION_BOUND = 5000;
    var arr = new Array();
    for (var i in points){
        var currentPoint = points[i];
        if (currentPoint[1] >= DISCONNECTION_BOUND){
            var leftPoint = points[i - 1];
            leftPoint[1] = 0;
            var rightPoint = [leftPoint[0] + currentPoint[1], 0];
            arr.push(leftPoint);
            arr.push(rightPoint);
            continue;
        }
        arr.push(points[i]);
    }
    return arr;
}

function getPLotPointsFromJsonSession(session){
    var t = session.start;
    console.log('session');
    console.log(session);
    var mas = new Array(session.rates.length);
    for (var i in session.rates){
        mas[i] = new Array(2);
        mas[i][0] = t;
        mas[i][1] = 60000.0 / session.rates[i];
        t+=session.rates[i];
    }
    //    console.log('mas');
    //    console.log(mas);
    return mas;
}

function filterIntervals(intervals){
    var bottom = 285;
    var top = 1714;
    var mas = new Array();
    
    var m = 0;
    for (var i in intervals){
        m+=intervals[i];
    }
    m = m / intervals.length;
    
    for (var i in intervals){
        if (i > 0){
            if ( ((intervals[i] < 0.8 * intervals[i-1] ) && (intervals[i] < 0.8 * intervals[i+1])) || ((intervals[i] > 1.2 * intervals[i-1] ) && (intervals[i] > 1.2 * intervals[i+1])) ){
                intervals[i] = (intervals[i-1] + intervals[i+1]) / 2.0;
            }
        }
        
        if (intervals[i] < 0.8 * m || intervals[i] > 1.2 * m){
            intervals[i] = m;
        }
        
        if (intervals[i] > top || intervals[i] < bottom){
            continue;
        }
        mas.push(intervals[i]);
    }
    return mas;
}

function getDispersionOfIntervals(intervals){
    //    console.log('getDispersionOfIntervals: intervals = ');
    //    console.log(intervals);
    intervals = filterIntervals(intervals);
    if (intervals.length < 2){
        return -10;
    }
    var m_x2 = 0;
    var m = 0;
    for (var i in intervals){
        m_x2+= intervals[i] * intervals[i];
        m+=intervals[i];
    }
    m_x2 = m_x2 / (1.0 * intervals.length);
    return Math.sqrt(m_x2 - m*m / (intervals.length * intervals.length));
}

function getDispersionPlotPointsFromJsonSession(session){
    var window_amount = 20;
    var k = 0;
    var t = session.start;
    var mas = new Array(session.rates.length);
    for (var i = 0; i < window_amount; i++){
        mas[i] = new Array(2);
        mas[i][0] = t;
        mas[i][1] = -10;
        t+=session.rates[i];
    }
    for (i = window_amount; i < session.rates.length; i++){
        var mas2 = new Array();
        for (var j = i - window_amount; j < i; j++){
            mas2.push(session.rates[j]);
        }
        var s = getDispersionOfIntervals(mas2);
        mas[i] = new Array(2);
        mas[i][0] = t;
        mas[i][1] = s;
        t+=session.rates[i];
    }
    
    return mas;
}

function getTensionPlotPointsFromJsonSession(session){
    var windowTime = 120000;
    var stepNumber = 10; //через каждые 10 интервалов пересcчитываем напряжение
    var mas = [];
    var sum = 0;
    var t = new Array(session.rates.length);
    t[0] = session.start;
    var k = 0;
    for (var i in session.rates){
        if (i == 0){
            continue;
        }
        t[i] = t[i-1] + session.rates[i];
    }
    //    var res = new Array((session.rates.length / stepNumber) + 1);
    var res = new Array();
    for (i = 0; i < session.rates.length; i+=stepNumber){
        mas = [];
        sum = 0;
        for (var j = i; j >= 0; j--){
            var rate = session.rates[j];
            sum+=rate;
            mas.push(rate);
            if (sum >= windowTime){
                break;
            }
        }
        if (sum < windowTime - 3000){
            continue;
        }
        var tension = getTension(mas);
        res.push([t[i], tension]);
    }
    console.log('getTensionPlotPointsFromJsonSession:');
    console.log(res);
    return res;
}

function getParameter(name) {
    name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
    results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function drawAllActivityPlots(sessions){
    var lastSigma = 100;
    for (var i in sessions){
        drawPlot('plot' + sessions[i].activityId , getPLotPointsFromJsonSession(sessions[i]), sessions[i].minPulse, sessions[i].maxPulse, true);
        var dispPlot = getDispersionPlotPointsFromJsonSession(sessions[i])
        drawPlot('dispPlot' + sessions[i].activityId , dispPlot , undefined, undefined);
        if (dispPlot[dispPlot.length - 1][1] != undefined && dispPlot[dispPlot.length - 1][1] > 0){
            lastSigma = dispPlot[dispPlot.length - 1][1];
        }
        var tensPlot = getTensionPlotPointsFromJsonSession(sessions[i]);
        drawPlot('tensionPlot' + sessions[i].activityId , tensPlot , 150, 150);
    }
    lastSigma = Math.round(100 * lastSigma) / 100.0;
    var gVal = (lastSigma >= 100) ? 0 : 100 - lastSigma;
    gauge.set(gVal);
    $('#gaugeSpanVal').text(lastSigma);
    console.log('lastSigma = ');
    console.log(lastSigma);
    
    initDispersionGauge(gup('workoutId'));
}

function initDispersionGauge(workoutId){
    var timeInterval = 3000;
    var window = 25000;
    var window_amount = 20;
    setInterval(function(){
        $.ajax({
            url: '/CardioSportWeb/resources/workout/last_intervals',
            type: 'POST',
            data: {
                workoutId : workoutId,
                timeInterval: window
            },
            success: function(data){
                var rates = data.data;
                if (rates == undefined){
                    return;
                }
                var sigma = getDispersionOfIntervals(rates.slice(Math.max(rates.length - window_amount - 1, 0), rates.length));
                //                var sigma = getDispersionOfIntervals(rates);
                sigma = Math.round(100 * sigma) / 100.0;
                var gVal = (sigma >= 100) ? 0 : 100 - sigma;
                gauge.set(gVal);
                $('#gaugeSpanVal').text(sigma);
            }
        });
    }, timeInterval);
}

function drawPlot(divId, points, minPulse, maxPulse, filterDisconnectionPointsFlag){
    console.log('points before disconnection filtering');
    console.log(points);
    console.log('points after disconnection filtering');
    if (filterDisconnectionPointsFlag != undefined){
        points = filterDisconnectionPoints(points);
    }
    console.log(points);
    
    if (points == undefined || points.length < 2){
        $('#'+divId).hide();
        return;
    }else{
        $('#'+divId).parent('div').show();
    }
    
    var minData = (minPulse == undefined) ? undefined : [[points[0][0],minPulse], [points[points.length - 1][0],minPulse]];
    var maxData = (maxPulse == undefined) ? undefined :  [[points[0][0],maxPulse], [points[points.length - 1][0],maxPulse]];
    
    var options = {
        series: {
            shadowSize: 0
            
        },
        yaxis: {
            color: '#92d5ea'
        },
        xaxis: {
            mode: "time",
            color: '#92d5ea'
        },
        grid: {
            borderWidth: 0
        }
    };
    
    if (minData == undefined || maxData == undefined){
        plot2 = $.plot($("#"+divId),
            [   {
                data: points, 
                lines: {
                    show:true
                
                }
            } ], options);
    }
    
    plot2 = $.plot($("#"+divId),
        [   {
            data: points, 
            lines: {
                show:true
                
            }
        },{
            data: minData,
            color : '#CF3300',
            lines: {
                show:true
                
            }
        },{
            data: maxData, 
            color : '#CF3300',
            lines: {
                show:true
            }
        } ], options);
}

function convertGps(gpsList){
    var mas = new Array(gpsList.length);
    for (var i in gpsList){
        mas[i] = new Array(2);
        mas[i][0] = gpsList[i].latitude;
        mas[i][1] = gpsList[i].longitude;
    }
    return mas;
}

function drawGoogleMap(divId, points){
    if (points == undefined || points.length == 0){
        $('#'+divId).hide();
        return;
    }
    myMap = new GMaps({
        div: '#'+divId,
        lat: points[0][0],
        lng: points[0][1],
        zoom: 18,
        scaleControl: false
    });
                    
    myMap.drawPolyline({
        path: points,
        strokeColor: '',
        strokeOpacity: 0.8,
        strokeWeight: 2
    });
}

function getGps(){
    var workoutId = getParameter("workoutId");
    console.log('loading gps');
    $.ajax({
        url: "/CardioSportWeb/resources/gps/gps",
        data: {
            workoutId : workoutId  
        },
        type: "POST",
        success: function(data){
            de = data;
            drawGoogleMap('map', convertGps(data.data));
        }
    });
}


            
function getAverageRR(points){
    var s = 0;
    for (var i in points){
        s+=points[i];
    }
    return 1.0 * s / points.length;
}
            
            
function getMo(points){
    var width = 50;
    var xmin = 300;
    var mas = [];
    for (var i=0; i <  Math.floor(1000 / width); i++){
        mas.push(0);
    }
    for (var i = 0; i < points.length; i++){
        mas[Math.floor((points[i] - xmin)/width)]++;
    }
                
    var res = 300;
    var max = 0;
    for (var i in mas){
        if (mas[i] > max){
            max = mas[i];
            res = xmin + i * width;
        }
    }
    return res;
}
            
function getAmo(points){
    var width = 50;
    var xmin = 300;
    var mas = [];
    for (var i=0; i <  Math.floor(1000 / width); i++){
        mas.push(0);
    }
    for (var i = 0; i < points.length; i++){
        mas[Math.floor((points[i] - xmin)/width)]++;
    }
                
    var max = 0;
    for (var i in mas){
        if (mas[i] > max){
            max = mas[i];
        }
    }
    return 100.0 * max / points.length;
}
            
function getBP(points){
    return getMax(points) - getMin(points);
}
            
function getMin(points){
    var min = 1500;
    for (var i in points){
        if (points[i] < min){
            min = points[i];
        }
    }
    return min;
}
function getMax(points){
    var max = 0;
    for (var i in points){
        if (points[i] > max){
            max = points[i];
        }
    }
    return max;
}
            
function getTension(points){
    var amo = getAmo(points);
    var mo = getMo(points);
    var vp = getBP(points);
                
    console.log('amo = ' + amo);
    console.log('mo = ' + mo);
    console.log('vp = ' + vp);
                
    return Math.floor(amo * 1000.0 * 1000.0 / (2.0 * mo * vp));
}