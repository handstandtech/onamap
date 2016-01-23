var util = {
    getStatesCount: function () {
        var count = 0;
        if (json.world.places != undefined) {
            var array = json.world.places['United States'].places;
            for (var stateName in array) {
                if (stateName != "District of Columbia") {
                    count++;
                }
            }
        }
        return count;
    },
    colorToHex: function (red, green, blue) {
        var rgb = blue | (green << 8) | (red << 16);
        return '#' + rgb.toString(16);
    }
};

var usMap = {
    R: undefined,
    defaultFillColor: "#d3d3d3",
    fillColors: new Object(),
    addFillColors: function () {
        if (json.world.places != undefined) {
            for (var stateAbbr in svgUS) {
                var stateName = json.states[stateAbbr];
                var theUs = json.world.places['United States'];
                var place = theUs.places[stateName];

                var color;
                if (place != undefined) {
                    color = usMap.calculateColor(place.photos.length);
                } else {
                    color = usMap.defaultFillColor;
                }
                usMap.fillColors[stateAbbr] = color;
            }
        }
    },
    calculateColor: function (count) {
        var colorMax = 165;
        var value = ((count / json.maxPhotosInState) * colorMax) / 2
            + (colorMax / 2) + (255 - colorMax) / 2;
        var hexColor = util.colorToHex(90, value, value);
        return hexColor;
    },
    stateClicked: function (stateAbbr) {
        var stateName = json.states[stateAbbr];
        var bottom = $("#bottom");
        bottom.animate({
            height: 200
        }, function () {
            $(window).resize();
        });
        bottom.empty();
        var theUs = json.world.places['United States'];
        var theState = theUs.places[stateName];
        var photosIds = theState.photos;
        if (photosIds != undefined) {
            stateName = stateName + ' (' + photosIds.length + ')';
        }
        bottom.append($('<h1>' + stateName + '</h1>'));
        console.log("PhotoIds: " + photosIds);
        for (var idx in photosIds) {
            console.log("idx: " + idx);
            var photoId = photosIds[idx];
            console.log("Photo ID: " + photoId);
            var photo = json.photosMap[photoId];
            console.log(photo);
            var img = $('<a href="' + photo.link + '" target="_blank"><img src="' + photo.url_sq + '"/></a>');
            bottom.append(img);
        }
    },
    paths: {},
    init: function () {
        usMap.R = new ScaleRaphael("us-map-svg", 959, 593);
        var attr = {
            "fill": usMap.defaultFillColor,
            "stroke": "#fff",
            "stroke-opacity": "1",
            "stroke-linejoin": "round",
            "stroke-miterlimit": "4",
            "stroke-width": ".75",
            "stroke-dasharray": "none"
        };

        var container = $("#content");

        function resizePaper() {

            var width = container.width();
            var height = container.height();
            usMap.R.changeSize(width, height, true, false);
        }

        resizePaper();
        $(window).resize(resizePaper);

        var textAttr = {
            'font': "30px Arial",
            'fill': "#111111"
        };
        usMap.R.text(500, 20, util.getStatesCount() + " out of 50 States!")
            .attr(textAttr);

        usMap.addFillColors();

        // Draw Map and store Raphael paths
        for (var stateAbbr in svgUS) {
            attr.fill = usMap.fillColors[stateAbbr];
            usMap.paths[stateAbbr] = usMap.R.path(svgUS[stateAbbr]).attr(attr)
                .data('id', stateAbbr);
        }

        usMap.addListeners();
    },
    addListeners: function () {
        // Do Work on Map
        for (var stateAbbr in svgUS) {
            // TODO HERE Add Correct Color
            usMap.paths[stateAbbr].color = usMap.fillColors[stateAbbr];

            (function (st, stateAbbr) {
                st[0].style.cursor = "pointer";

                var TIME = 1000;

                function fadeOut(st) {
                    if (usMap.paths[stateAbbr].color != usMap.defaultFillColor) {
                        st.animate({
                            fill: "#333"
                            // fill : Raphael.getColor()
                        }, TIME, 'ease-out');
                    }
                }

                function fadeIn(st) {
                    st.animate({
                        fill: usMap.paths[stateAbbr].color
                    }, TIME, 'ease-in', function () {
                        fadeOut(st);
                    });
                }

                st[0].onmouseover = function () {
                    fadeOut(st);
                    st.toFront();
                    usMap.R.safari();
                };

                st[0].onclick = function () {
                    var stateAbbr = st.data('id').toUpperCase();
                    usMap.stateClicked(stateAbbr);
                };

                st[0].onmouseout = function () {
                    st.stop();
                    st.animate({
                        fill: usMap.paths[stateAbbr].color
                    }, 500);
                    st.toFront();
                    usMap.R.safari();
                };

            })(usMap.paths[stateAbbr], stateAbbr);
        }
    }
};

// --------------------

var gmap = {
    map: undefined,
    init: function () {
        var width = $("#content").width();
        var height = $("#content").height();
        var myLatlng = new google.maps.LatLng(37.615162, -76.292157);

        var myOptions = {
            zoom: 3,
            center: myLatlng,
            mapTypeId: google.maps.MapTypeId.TERRAIN
        };

        gmap.map = new google.maps.Map(document.getElementById("google-map"),
            myOptions);

        for (var photoId in json.photosMap) {
            var photo = json.photosMap[photoId];
            gmap.addPhoto(photo);
        }
    },
    reset: function () {
        google.maps.event.trigger(gmap.map, 'resize');
    },
    infowindow: new google.maps.InfoWindow({content: ""}),
    formatTime: function (time) {
        var d = new Date(time),
            month = d.getMonth(),
            day = d.getDate() ,
            year = d.getFullYear();

        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;

        var monthNames = ["January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        ];

        return monthNames[month] + " " + day + ", " + year;
    },
    image: new google.maps.MarkerImage(
        '/assets/img/mm_20_red.png'),
    shadow: new google.maps.MarkerImage(
        '/assets/img/mm_20_shadow.png'),
    addPhoto: function (photo) {
        var lat = photo.lat, lng = photo.lng, title = photo.title, imgsrc = photo.url_s, link = photo.link;
        var latlng = new google.maps.LatLng(lat, lng);
        var contentString = '<div id="content" class="text-center">' +
            '<strong>' + title + '</strong><br/>' +
            '<span style="font-size: 80%">' + this.formatTime(photo.datetaken) + '</span><br/>' +
            '<a href="' + link + '" target="_blank"><img src="' + imgsrc + '"/></a>' +
            '</div>';
        var marker = new google.maps.Marker({
            position: latlng,
            map: gmap.map,
            title: title,
            shadow: gmap.shadow,
            icon: gmap.image
        });
        var self = this;
        google.maps.event.addListener(marker, 'click', function () {
            self.infowindow.setContent(contentString);
            self.infowindow.open(gmap.map, marker);
        });
    }
};

// ---------------
$(function () {
    usMap.init();
    gmap.init();
    var content = $("#content");
//    var tabs = $("ul#tabs li");

    window.showTab = function (idx) {
        content.find(">div").each(function () {
            $(this).css({
                zIndex: 0
            });
        });
        content.find(">div:nth-child(" + (idx + 1) + ")").css({
            zIndex: 100
        });
        gmap.reset();
    }

//    tabs.click(function () {
//        var clicked = $(this);
//        var idx = tabs.index(clicked);
//        showTab(idx);
//    });

    showTab(0);
    setTimeout(function () {
        $(window).resize();
    }, 1000);
});


var app = angular.module('myApp', ['appControllers']);
var appControllers = angular.module('appControllers', []);

appControllers.controller('TimelineCtrl', ['$scope', '$rootScope', '$http',
    function ($scope, $rootScope, $http) {

        var photos = [];

        for (var photoId in window.json.photosMap) {
            var photo = window.json.photosMap[photoId];
            photos.push(photo);
        }

        function sortByDate(a, b) {
            if (a.datetaken > b.datetaken)
                return -1;
            if (a.datetaken < b.datetaken)
                return 1;
            // a must be equal to b
            return 0;
        };

        $scope.chunk = function (arr, size) {
            var newArr = [];
            for (var i = 0; i < arr.length; i += size) {
                newArr.push(arr.slice(i, i + size));
            }
            return newArr;
        }

        $scope.sort = function (sortType) {
            if (sortType == 'date') {
                photos.sort(sortByDate);
            } else if (sortType == 'date') {
                photos.sort(sortByDate);
            }


            $scope.photos = photos;

            $scope.rows = $scope.chunk(photos, 3);
        }

        $scope.sort('date');

        $scope.world = {"me": "you"};

    }
]);