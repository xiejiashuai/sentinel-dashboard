var app = angular.module('sentinelDashboardApp');

app.controller('MachineCtl', ['$scope', '$stateParams', '$http','$httpParamSerializerJQLike', 'MachineService',
    function ($scope, $stateParams, $http,$httpParamSerializerJQLike, MachineService) {
        $scope.app = $stateParams.app;
        $scope.propertyName = '';
        $scope.reverse = false;
        $scope.currentPage = 1;
        $scope.machines = [];
        $scope.machinesPageConfig = {
            pageSize: 10,
            currentPageIndex: 1,
            totalPage: 1,
            totalCount: 0,
        };

        $scope.sortBy = function (propertyName) {
            // console.log('machine sortBy ' + propertyName);
            $scope.reverse = ($scope.propertyName === propertyName) ? !$scope.reverse : false;
            $scope.propertyName = propertyName;
        };

        $scope.reloadMachines = function() {
            MachineService.getAppMachines($scope.app).success(
                function (data) {
                    // console.log('get machines: ' + data.data[0].hostname)
                    if (data.code == 0 && data.data) {
                        $scope.machines = data.data;
                        var health = 0;
                        $scope.machines.forEach(function (item) {
                            if (item.health) {
                                health++;
                            }
                            if (!item.hostname) {
                                item.hostname = '未知'
                            }
                        })
                        $scope.healthCount = health;
                        $scope.machinesPageConfig.totalCount = $scope.machines.length;
                    } else {
                        $scope.machines = [];
                        $scope.healthCount = 0;
                    }
                }
            );
        };

        $scope.removeMachine = function(ip, port) {
            if (!confirm("您确定要删除机器 [" + ip + ":" + port + "]码?")) {
                return;
            }
            $http({
                url: 'app/' + $scope.app + '/machine/remove.json',
                method: 'POST',
                headers: {
                    "Content-type": 'application/x-www-form-urlencoded; charset=UTF-8'
                },
                data: $httpParamSerializerJQLike({
                    ip: ip,
                    port: port
                })
            }).success(
                function(data) {
                    if (data.code == 0) {
                        $scope.reloadMachines();
                    } else {
                        alert("删除该机器失败");
                    }
                }
            );
        };

        $scope.reloadMachines();

    }]);