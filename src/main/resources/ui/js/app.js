var app = angular.module('app', [ ]);

app.controller('FlightController', function($http, $log, $timeout) {
	var vm = this;
    var baseUrl = 'http://localhost:8080/ardrone/executeOverride/';
    var controllerId = 'controller-id';

    function getTime() {
        return new Date().toLocaleTimeString().replace("/.*(\d{2}:\d{2}:\d{2}).*/", "$1");;
    }

    var executeOverride = function (action) {
        vm.activityLog.push({time: getTime(), message: 'Sending ' + action + ' action'});
        vm.sendingAction = true;
        return $http.get(baseUrl + action).then(function (response) {
            $log.info(response.data);
            vm.activityLog.push({time: getTime(), message: response.data});
            return response.data;
        }, function (response) {
            vm.activityLog.push({time: getTime(), message: 'Error sending ' + action + ' action to AR Parrot Drone'});
            return response;
        }).then(function (response) {
            vm.sendingAction = false;
            return response;
        });
    };

    vm.activityLog = [];

    $timeout(function () {
        $('#' + controllerId).focus();
        $('#' + controllerId).bind('keydown', function(event) {
            var keyCodeAction = keyCodeActionMap[event.keyCode];
            if(keyCodeAction) {
                keyCodeAction();
			}
        });
    });

    vm.clearActivityLog = function(){
        vm.activityLog = [];
	};

    vm.up = function(){
        executeOverride('UP');
    };

    vm.down = function(){
        executeOverride('DOWN');
    };

    vm.left = function(){
        executeOverride('LEFT');
    };

    vm.right = function(){
        executeOverride('RIGHT');
    };

    vm.forward = function(){
        executeOverride('FORWARD');
    };

    vm.reverse = function(){
        executeOverride('REVERSE');
    };

    vm.takeOff = function(){
        executeOverride('TAKEOFF');
    };

    vm.land = function(){
        executeOverride('LAND');
    };

    vm.shutdown = function(){
        executeOverride('SHUTDOWN');
    };

    var keyCodeActionMap = {
    	37: vm.left,
    	38: vm.up,
    	39: vm.right,
    	40: vm.down,

    	27: vm.shutdown,

    	17: vm.reverse,
    	32: vm.forward,
    	33: vm.takeOff,
    	34: vm.land
	};

});