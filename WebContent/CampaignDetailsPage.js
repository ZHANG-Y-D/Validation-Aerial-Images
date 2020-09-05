(function() { // avoid variables ending up in the global scope


    var contentDiv = document.getElementById("contentDiv");
    var messageDiv = document.getElementById("messageDiv");

    var downloadImage = new DownloadImage();
    var submitImage = new SubmitImage();
    var showImageInMap = new ShowImageInMap();
    var pageOrchestrator = new PageOrchestrator();
    var printCampaignDetails = new PrintCampaignDetails();
    var campaignName = sessionStorage.getItem("CampaignName");

    window.addEventListener("load", () => {
        pageOrchestrator.start(); // initialize the components
        pageOrchestrator.refresh(); // display initial content
    }, false);

    function PageOrchestrator(){

        this.start = function(){
            printCampaignDetails.show();
            downloadImage.show();
            submitImage.show();
        }

        this.refresh = function(){

        }

    }

    function PrintCampaignDetails(){
        var self = this;

        this.show = function () {

            makeCall("GET", "GetCampaignDetails?CampaignName="+campaignName, null,function (req) {
                if (req.readyState === XMLHttpRequest.DONE) {
                    var message = req.responseText;
                    if (req.status === 200) {
                        self.update(JSON.parse(req.responseText));
                    } else {
                        messageDiv.innerHTML = "";
                        messageDiv.textContent = message;
                    }
                }
            })
        }

        this.update = function (campaign){
            let campaignName = document.createElement("div");
            let campaignClient = document.createElement("div");
            let campaignManager = document.createElement("div");
            let campaignStatus = document.createElement("div");
            campaignName.textContent = "Campaign Name: "+campaign.name;
            campaignClient.textContent = "Client: "+campaign.client;
            campaignManager.textContent = "Manager: "+campaign.manager;
            campaignStatus.textContent = "Campaign status: "+campaign.status;
            contentDiv.appendChild(campaignName);
            contentDiv.appendChild(campaignClient);
            contentDiv.appendChild(campaignManager);
            contentDiv.appendChild(campaignStatus);
        }


    }


    function DownloadImage(){
        var self = this;

        this.show = function () {
            makeCall("GET", "DownloadImage", null,
                function (req) {
                    if (req.readyState === XMLHttpRequest.DONE) {
                        var message = req.responseText;
                        if (req.status === 200) {
                            self.update(JSON.parse(req.responseText));
                        } else {
                            messageDiv.innerHTML = "";
                            messageDiv.textContent = message;
                        }
                    }
            })
        }

        this.update = function (imageList){

            if (imageList.length === 0) {
                messageDiv.innerHTML = "";
                messageDiv.textContent = "No image yet!";
            } else {
                var imageFeatures = [];
                imageList.forEach( image => {
                    var feature = {
                        latitude: image.latitude,
                        longitude: image.longitude,
                        icon: "data:image/png;base64," + image.foto
                    };

                    imageFeatures.push(feature);

                });

                sessionStorage.setItem('ImageFeatures', JSON.stringify(imageFeatures));
                showImageInMap.show();
            }



        }

    }

    function ShowImageInMap(){
        var self = this;

        this.show = function () {
            // Create the script tag, set the appropriate attributes
            var script = document.createElement('script');
            script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyCylFo3YbqzCjrKEqGBmmliafIHwuXHdHc&callback=initMap';
            script.defer = true;
            // Append the 'script' element to 'head'
            document.body.appendChild(script);
        }
        this.update = function (){
        };

    }

    function SubmitImage(){
        var self = this;

        this.show = function () {
            document.getElementById("submitbutton").addEventListener('click', (e) => {

                var form = e.target.closest("form");

                if (form.checkValidity()) {
                    makeCall("POST", 'SubmitImage', e.target.closest("form"),
                        function (req) {
                            if (req.readyState === XMLHttpRequest.DONE) {
                                var message = req.responseText;
                                if (req.status === 200) {
                                    window.location.href = "CampaignDetailsPage.html";
                                } else {
                                    messageDiv.innerHTML = "";
                                    messageDiv.textContent = message;
                                }
                            }
                        }
                    );
                } else {
                    form.reportValidity();
                }
            });
        }

    }

})();