(function(){

    var subscribedDiv = document.getElementById("subscribedCampainsDiv");
    var notSubscribedDiv = document.getElementById("notSubscribedCampainsDiv");
    var messageDiv = document.getElementById("workerHomeAlert");
    var userNameTag = document.getElementById("username");

    var pageOrchestrator = new PageOrchestrator();

    var subscribedCampaignList, notSubscribedCampaignList, subscribeToCampaign, imageList, submitAnnotation;

    window.addEventListener("load", () => {
        pageOrchestrator.start(); // initialize the components
    }, false);


    function PageOrchestrator(){
        this.start = function (){

            ShowUserProfile(userNameTag, messageDiv);
            subscribedCampaignList = new SubscribedCampaignList();
            notSubscribedCampaignList = new NotSubscribedCampaignList();
            subscribeToCampaign = new SubscribeToCampaign();

            subscribedCampaignList.show();
            notSubscribedCampaignList.show();
        }

    }


    function SubscribedCampaignList() {
        var self = this;

        this.show = function () {
            makeCall("GET", "GetSubscribedCampaigns", null,
                function (req) {
                    if (req.readyState === 4) {
                        var message = req.responseText;
                        if (req.status === 200) {
                            self.update(JSON.parse(req.responseText));
                        } else {
                            subscribedDiv.innerHTML = "";
                            subscribedDiv.textContent = message;
                        }
                    }
                })
        }

        this.update = function (campaignList) {
            if (campaignList.length === 0) {
                subscribedDiv.innerHTML = "";
                subscribedDiv.textContent = "You are not subscribed to any started campaigns!";
            } else {
                var title = document.createElement("p");
                title.textContent = "Your subscribed campaigns:"
                subscribedDiv.appendChild(title);
                var list = document.createElement("ul");
                subscribedDiv.appendChild(list);

                campaignList.forEach(campaign => {

                    var li = document.createElement("li");
                    list.appendChild(li);
                    var anchor = document.createElement("a");
                    li.appendChild(anchor);
                    anchor.textContent = campaign;
                    anchor.setAttribute('campaignName', campaign);

                    anchor.addEventListener("click", (e) => {

                        if (typeof (Storage) !== "undefined") {
                            sessionStorage.setItem("CampaignName",e.target.getAttribute("campaignName"));
                            window.location.href = "writeAnnotationPage.html";
                        } else {
                            messageDiv.innerHTML = "";
                            messageDiv.innerHTML = "Sorry, your browser does not support Web Storage...";
                        }

                       // imageList.show( e.target.getAttribute("campaignName"));

                    }, false);
                    // anchor.href = "#";
                });

            }
        }
    }

    function NotSubscribedCampaignList() {
        var self = this;

        this.show = function () {
            makeCall("GET", "GetNotSubscribedCampaigns", null,
                function (req) {
                    if (req.readyState === 4) {
                        var message = req.responseText;
                        if (req.status === 200) {
                            self.update(JSON.parse(req.responseText));
                        } else {
                            notSubscribedDiv.innerHTML = "";
                            notSubscribedDiv.textContent = message;
                        }
                    }
                })
        }

        this.update = function (campaignList) {
            if (campaignList.length === 0) {
                notSubscribedDiv.innerHTML = "";
                notSubscribedDiv.textContent = "No new campaigns!";
            } else {
                var title = document.createElement("p");
                title.textContent = "Your can subscribe to the following campaigns:"
                notSubscribedDiv.appendChild(title);
                var list = document.createElement("ul");
                notSubscribedDiv.appendChild(list);

                campaignList.forEach(campaign => {

                    var li = document.createElement("li");
                    list.appendChild(li);
                    var anchor = document.createElement("a");
                    li.appendChild(anchor);
                    anchor.textContent = campaign;
                    anchor.setAttribute('campaignName', campaign);

                    anchor.addEventListener("click", (e) => {

                        sessionStorage.setItem("CampaignName", e.target.getAttribute("campaignName"))
                        subscribeToCampaign.show(e.target.getAttribute("campaignName"));

                    }, false);
                    // anchor.href = "#";
                });

            }

        }


    }

    function SubscribeToCampaign(){
        var self = this;

        this.show = function (cname){
            makeCall("GET", "subscribeToCampaign?campaignName="+cname, null,
                function (req) {
                    if (req.readyState === 4) {
                        var message = req.responseText;
                        if (req.status === 200) {
                            //todo controllare
                            if (typeof (Storage) !== "undefined") {
                                sessionStorage.setItem("campaignname",cname);
                                window.location.href = "writeAnnotationPage.html";
                            } else {
                                messageDiv.innerHTML = "";
                                messageDiv.innerHTML = "Sorry, your browser does not support Web Storage...";
                            }

                            //imageList.show(cname);
                        } else {
                            messageDiv.innerHTML = "";
                            messageDiv.textContent = message;
                        }
                    }
                })

        }
    }

})();