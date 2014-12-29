var util = {
    getStatesCount: function () {
        var count = 0;
        if (json.world.places != undefined) {
            var array = json.world.places['United States of America'].places;
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
                var theUs = json.world.places['United States of America'];
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
        var theUs = json.world.places['United States of America'];
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
            var img = $('<img src="' + photo.url_sq + '"/>');
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

            var width = container.width() - 20;
            var height = container.height() - 10;
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
        // $("#google-map").css({width: width, height: height});
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
            gmap.addPhoto(photo.lat, photo.lng, photo.title, photo.url_s);
        }
    },
    reset: function () {
        google.maps.event.trigger(gmap.map, 'resize');
    },
    image: new google.maps.MarkerImage(
        'http://labs.google.com/ridefinder/images/mm_20_red.png'),
    shadow: new google.maps.MarkerImage(
        'http://labs.google.com/ridefinder/images/mm_20_shadow.png'),
    addPhoto: function (lat, lng, title, imgsrc) {
        var latlng = new google.maps.LatLng(lat, lng);
        var contentString = '<div id="content">' + title + '<br/><img src="'
            + imgsrc + '"/></div>';
        var infowindow = new google.maps.InfoWindow({
            content: contentString
        });

        var marker = new google.maps.Marker({
            position: latlng,
            map: gmap.map,
            title: title,
            shadow: gmap.shadow,
            icon: gmap.image
        });
        google.maps.event.addListener(marker, 'click', function () {
            infowindow.open(gmap.map, marker);
        });
    }
};

$(function () {
    usMap.init();
    gmap.init();
});

// ---------------
$(function () {
    var content = $("#content");
    var tabs = $("ul#tabs li");

    function showTab(idx) {
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

    tabs.click(function () {
        var clicked = $(this);
        var idx = tabs.index(clicked);
        showTab(idx);
    });

    showTab(0);
});
