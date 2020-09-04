function initMap() {
    // The location of Uluru
    var uluru = {lat: 45.46, lng: 9.19};
    // The map, centered at Uluru
    var map = new google.maps.Map(
        document.getElementById('map'), {zoom: 3, center: uluru});

    let features = JSON.parse(sessionStorage.getItem('ImageFeatures'));

    for (var i = 0; i < features.length; i++) {

        var image = {
            url: features[i].icon,
            size: new google.maps.Size(71, 71),
            scaledSize: new google.maps.Size(25, 25)

        }
        
        var marker = new google.maps.Marker({
            position: features[i].position,
            icon: image,
            map: map
        });
    }
}