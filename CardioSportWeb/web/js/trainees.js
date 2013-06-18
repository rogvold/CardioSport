function createUserManual(array){
    UserMan = {};
    if (array == undefined){
        return;
    }
    for (var i in array){
        UserMan[array[i].id] = {
            firstName: array[i].firstName,
            lastName: array[i].lastName,
            id: array[i].id
        }
    }
}

function loadUserMan(){
    $.ajax({
        url: "/CardioSportWeb/resources/auth/coach_trainees",
        type: "POST",
        success: function(data){
            de = data;
            createUserManual(data.data);
        },
        error: function(){
            console.log('loadManual: ajax error...');
        }
    });
}

function loadWorkouts(){
    $.ajax({
        url: "/CardioSportWeb/resources/workout/get_all",
        type: "POST",
        success: function(data){
            de = data;
            workouts = data.data;
        },
        error: function(){
            console.log('loadManual: ajax error...');
        }
    });
}

function loadAll(){
    loadUserMan();
    loadWorkouts();
    initActivitiesListHTML('#workouts_list_dialog');
    
    $('.appointment_button').live('click', function(){
        selectUser($(this).attr('data-id'));
        $('#trigger-id').click();
    });
    
    $('.appoint_workout_button_plus').live('click', function(){
        appointWorkout(selectedUser.id, $(this).attr('data-id'));
    });
    
    updateUsersState();
    setInterval(function(){
        updateUsersState();
    }, 5000);
}

function prepareWorkoutsDialog(){
    
}

function selectUser(userId){
    selectedUser = UserMan[userId];
}

function appointWorkout(traineeId, workoutId){
    $.ajax({
        url: "/CardioSportWeb/resources/workout/appoint_workout",
        data:{
            traineeId: traineeId,
            workoutId: workoutId
        },
        type: "POST",
        success: function(data){
            de = data;
            alert('Пользователю ' + UserMan[traineeId].firstName + " " + UserMan[traineeId].lastName + " назначена выбранная тренировка");
            window.location.reload();
        },
        error: function(){
            console.log('loadManual: ajax error...');
        }
    });
}

function updateUsersState(){
    $.ajax({
        url: "/CardioSportWeb/resources/workout/trainees_state",

        type: "POST",
        success: function(data){
            de = data;
            for (var i in data.data){
                updateUserState(data.data[i]);
            }
        },
        error: function(){
            console.log('loadManual: ajax error...');
        }
    });
}

function updateUserState(state){
    console.log('updateUserState: state = ');
    console.log(state);
    
    console.log('oldPulse = ' + $('.pulse_label[data-id="'+state.userId+'"]').text());
    $('.pulse_label[data-id="'+state.userId+'"]').text((state.pulse == undefined) ? 'N/A' : state.pulse);
    $('.speed_label[data-id="'+state.userId+'"]').text((state.speed == undefined) ? 'N/A' : state.speed + ' км/ч');
    $('.distance_label[data-id="'+state.userId+'"]').text((state.distance == undefined) ? 'N/A' : state.distance + ' m');
}