function initMap() {
    // The location of Uluru
    var uluru = {lat: 45.46, lng: 9.19};
    let features = JSON.parse(sessionStorage.getItem('ImageFeatures'));

    // The map, centered at Uluru
    var map = new google.maps.Map(document.getElementById('map'), {zoom: 3, center: uluru});

    if(features != null){

        features.forEach( feature => {

            var image = {
                url: feature.icon,
                size: new google.maps.Size(71, 71),
                scaledSize: new google.maps.Size(25, 25)

            }

            var marker = new google.maps.Marker({
                position: new google.maps.LatLng(feature.latitude, feature.longitude),
                icon: image,
                map: map
            });
        });
    }

    sessionStorage.removeItem('ImageFeatures')
}