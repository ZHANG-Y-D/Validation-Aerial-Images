(function(){

    var notSubscribedDiv = document.getElementById("subscribedCampainsDiv");
    var notSubscribedDiv = document.getElementById("notSubscribedCampainsDiv");

    var pageOrchestrator = new PageOrchestrator();

    var subscribedCampaignList, notSubscribedCampaignList;

    window.addEventListener("load", () => {
        pageOrchestrator.start(); // initialize the components
        pageOrchestrator.refresh(); // display initial content
    }, false);


    function PageOrchestrator(){
        this.start = function (){
            subscribedCampaignList = new SubscribedCampaignList();
            notSubscribedCampaignList = new NotSubscribedCampaignList();
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
                            campaignListDiv.innerHTML = "";
                            campaignListDiv.textContent = message;
                        }
                    }
                })
        }

        this.update = function (campaignList) {
            if (campaignList.length === 0) {
                notSubscribedDiv.innerHTML = "";
                notSubscribedDiv.textContent = "You are not subscribed to any campaigns!";
            } else {
                var title = document.createElement("p");
                title.textContent = "Your subscribed campaigns:"
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
                        window.location.href = "writeAnnotationPage.html";


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
                            campaignListDiv.innerHTML = "";
                            campaignListDiv.textContent = message;
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
                        window.location.href = "writeAnnotationPage.html";


                    }, false);
                    // anchor.href = "#";
                });

            }

        }


    }

})();