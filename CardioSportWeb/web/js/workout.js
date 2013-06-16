


function initActivitiesListHTML(divId){
    $('#' + divId).children('.scroll').nanoScroller();
    //                $('#support-tickets .support-msg').append('<span class="support-full">VIEW FULL TICKET</span>');
    $('#' + divId + ' li').bind('click', function() {
        var supMsgHeight = $(this).children('.support-msg').height() + 220;
        contHeight = $('#' + divId).outerHeight();
        liPosTop = $(this).position().top;
        if ( $(this).hasClass('expanded') ) {
            $(this).removeClass('expanded').removeAttr('style');
        } else {
            $(this).addClass('expanded').css('height', supMsgHeight)
            .siblings('li.expanded').removeClass('expanded').removeAttr('style');
        };
        if ( liPosTop + supMsgHeight > contHeight ) {
            $(this).parents('.scroll-cont').animate({
                scrollTop: supMsgHeight
            }, 600);
        } else if ( $(this).is(':nth-last-child(-n+3)') ) {
            $(this).parents('.scroll-cont').animate({
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
        url: "/CardioSportWeb/resources/activity/activities_list",
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
        },
        error: function(){
        //            registrationFailedCallback("Проверьте подключение к интернету");
        }
    });
}

function generateActivityInfo(actId){
    var activity = Man[actId];
    return '<li> <span class="support-name">' + activity.name + '</span> <span class="support-usr" style="margin-right: 120px;" ><b>' + activity.duration +'</b> мин.</span> <p class="support-msg">' + activity.description + ' <table class="g16" style="margin-left: 10px;"> <tbody> <tr> <td style="width: 240px;">Минимальный пульс</td> <td class="orange-text">'+activity.minHeartRate+'</td> </tr> <tr> <td>Максимальный пульс</td> <td class="orange-text">'+activity.maxHeartRate+'</td> </tr> <tr> <td>Минимальная скорость</td> <td class="orange-text">'+activity.minSpeed+' км/ч</td> </tr> <tr> <td>Максимальная скорость</td> <td class="orange-text">'+activity.maxSpeed+' км/ч</td> </tr> <tr> <td>Минимальное напряжение</td> <td class="orange-text">'+activity.minTension+'</td> </tr> <tr> <td>Максимальное напряжение</td> <td class="orange-text">'+activity.maxTension+'</td> </tr> </tbody> </table> </p> </li>';
}

function generateEctivitiesInfoList(list){
    var preHtml = '  <div class="scroll has-scrollbar"> <ul class="ul-grad scroll-cont" tabindex="0" style="right: -15px;">';
    var postHtml = '</ul> <div class="pane" style="display: block;"><div class="slider" style="height: 213px; top: 22.12987012987013px;"></div></div></div> ';
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
    initActivitiesListHTML('selected-activities');
    $('.add_button').bind('click', function(){
        var actId = $(this).attr('data-id');
        addActivity(actId);
        console.log(pool);
        //        console.log(getPoolHtml());
        $('#selected-activities').html(getPoolHtml());
        initActivitiesListHTML('selected-activities');
    });
    
    $('#workout_name_input').keyup(function() {
        if((pool.length > 0) && ($('#workout_name_input').val() !="")){
            $('#create_button').show();
        }else{
            $('#create_button').hide();

        }
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
    initActivitiesListHTML('selected-activities');

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
    return generateEctivitiesInfoList(pool);
}