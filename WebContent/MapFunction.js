function initMap(listener) {
    // The location of Uluru
    var uluru = {lat: 45.46, lng: 9.19};
    let features = JSON.parse(sessionStorage.getItem('ImageFeatures'));

    // The map, centered at Uluru
    var map = new google.maps.Map(document.getElementById('map'), {zoom: 2, center: uluru});

    if(features != null){

        features.forEach(feature => {

            var image = {
                url: feature.icon,
                size: new google.maps.Size(71, 71),
                origin: new google.maps.Point(0, 0),
                anchor: new google.maps.Point(17, 34),
                scaledSize: new google.maps.Size(25, 25)
            }


            var marker = new google.maps.Marker({
                position: new google.maps.LatLng(feature.latitude, feature.longitude),
                icon: image,
                map: map,
                imageId:feature.imageId
            });

            google.maps.event.addListener(marker, 'click', function(e) {
                sessionStorage.setItem("ImageId",marker.imageId);
                window.location.href = "imageDetails.html";
            });
        });
    }
    sessionStorage.removeItem('ImageFeatures')
}

