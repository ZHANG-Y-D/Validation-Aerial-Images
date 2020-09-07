(function() { // avoid variables ending up in the global scope


    const contentDiv = document.getElementById("contentDiv");
    let messageDiv = document.getElementById("messageDiv");
    const avviaButton = document.getElementById("avviaButton");
    const chiudereButton = document.getElementById("chiudereButton");
    const imageForm = document.getElementById("imageForm");
    const statisticTag = document.getElementById("StatisticTag");



    const downloadImage = new DownloadImage();
    const submitImage = new SubmitImage();
    const showImageInMap = new ShowImageInMap();
    const pageOrchestrator = new PageOrchestrator();
    const printCampaignDetails = new PrintCampaignDetails();
    const changeCampaignStatus = new ChangeCampaignStatus();
    const statistic = new Statistic();
    let campaignName = null;
    let campaignStatus = null;


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
            campaignName = getSession("CampaignName",messageDiv);

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
            let campaignStatusTag = document.createElement("div");
            campaignName.textContent = "Campaign Name: "+campaign.name;
            campaignClient.textContent = "Client: "+campaign.client;
            campaignManager.textContent = "Manager: "+campaign.manager;
            campaignStatus = campaign.status;
            campaignStatusTag.textContent = "Campaign status: "+campaignStatus;
            contentDiv.appendChild(campaignName);
            contentDiv.appendChild(campaignClient);
            contentDiv.appendChild(campaignManager);
            contentDiv.appendChild(campaignStatusTag);
            switch(campaignStatus) {
                case "CREATED":
                    imageForm.style.visibility = "visible";
                    avviaButton.style.visibility = "hidden";
                    chiudereButton.style.visibility = "hidden";
                    statisticTag.style.visibility = "hidden";
                    break
                case "STARTED":
                    changeCampaignStatus.show("chiudere");
                    statistic.show();
                    break
                case "CLOSED":
                    imageForm.style.visibility = "hidden";
                    avviaButton.style.visibility = "hidden";
                    chiudereButton.style.visibility = "hidden";
                    statistic.show();
                    break
                default:
                    messageDiv = ""
                    messageDiv.textContent = "Status error"
            }
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
                if (campaignStatus === "CREATED") {
                    changeCampaignStatus.show("avvia");
                }
                var imageFeatures = [];
                imageList.forEach( image => {
                    var feature = {
                        latitude: image.latitude,
                        longitude: image.longitude,
                        icon: "data:image/png;base64," + image.foto,
                        imageId: image.id
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

    function ChangeCampaignStatus(){
        var self = this;

        this.show = function (status) {
            if (status === "avvia"){
                avviaButton.style.visibility = "visible";
                avviaButton.addEventListener("click", (e) => {
                    self.update(1);
                }, false);
            }
            if (status === "chiudere"){
                chiudereButton.style.visibility = "visible";
                avviaButton.style.visibility = "hidden";
                imageForm.style.visibility = "hidden";
                chiudereButton.addEventListener("click", (e) => {
                    self.update(2);
                }, false);
            }
        }

        this.update = function (status){
            makeCall("GET", "ChangeCampaignStatus?Status="+status, null,
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
            })
        }
    }

    function Statistic(){
        var self = this;

        this.show = function (){
            statisticTag.style.visibility = "visible";
            statisticTag.addEventListener("click", (e) => {
                self.update();
            }, false);
        }

        this.update = function (){
            makeCall("GET", "GetStatistics", null,
                function (req) {
                    if (req.readyState === XMLHttpRequest.DONE) {
                        var message = req.responseText;
                        if (req.status === 200) {
                            // window.location.href = "CampaignDetailsPage.html";
                        } else {
                            messageDiv.innerHTML = "";
                            messageDiv.textContent = message;
                        }
                    }
            })
        }

    }

})();