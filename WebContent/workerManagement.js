(function(){

    var subscribedDiv = document.getElementById("subscribedCampainsDiv");
    var notSubscribedDiv = document.getElementById("notSubscribedCampainsDiv");
    var messageDiv = document.getElementById("workerHomeAlert");


    var imageDiv = document.getElementById("imagesDiv");
    var annotationForm = document.getElementById("annotationForm");
    annotationForm.style.visibility = "hidden";
    var notificatioMessage = document.getElementById("notificationMessage");
    var annotationAlert = document.getElementById("annotationAlert");


    var annotationMessageDiv = document.getElementById("annotationAlert");

    var pageOrchestrator = new PageOrchestrator();

    var subscribedCampaignList, notSubscribedCampaignList, subscribeToCampaign, imageList, submitAnnotation;

    window.addEventListener("load", () => {
        pageOrchestrator.start(); // initialize the components
    }, false);


    function PageOrchestrator(){
        this.start = function (){
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

                       // sessionStorage.setItem("CampaignName", e.target.getAttribute("campaignName"))
                        //todo controllare
                        window.location.href = "writeAnnotationPage.html";
                        imageList.show( e.target.getAttribute("campaignName"));

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
                            window.location.href = "writeAnnotationPage.html";
                            imageList.show(cname);
                        } else {
                            messageDiv.innerHTML = "";
                            messageDiv.textContent = message;
                        }
                    }
                })

        }
    }

    function ImageList() {
        var self = this;

        this.show = function (cname){
            makeCall("GET", "GetNotAnnotatedImages?CampaignName="+cname, null,
                function (req) {
                    if (req.readyState === 4) {
                        var message = req.responseText;
                        if (req.status === 200) {
                            //todo controllare
                            sessionStorage.setItem('campaignname', cname);
                           self.update(JSON.parse(req.responseText));

                        } else {
                            annotationMessageDiv.innerHTML = "";
                            annotationMessageDiv.textContent = message;
                        }
                    }
                })


        }

        this.update = function (images){

            var imageUL = document.createElement("ul");
            imageDiv.appendChild(imageUL);

            images.forEach( image => {
                var imageLI = document.createElement("li")
                imageUL.appendChild(imageLI);
                var anchor = document.createElement("a");
                imageLI.appendChild(anchor);

                var imageTag = document.createElement("img");

                imageTag.src = "data:image/png;base64,"+image.foto;
                //todo da mettere con CSS
                imageTag.width = 150;
                imageTag.height = 150;
                anchor.appendChild(imageTag);

                anchor.setAttribute('id', image.id);

                anchor.addEventListener("click", (e) => {
                    submitAnnotation.show(e.target.getAttribute("id"));
                    annotationForm.imageId.value = e.target.getAttribute("id");
                    annotationForm.style.visibility = "visible";
                }, false);


            });
        }



    }

    function SubmitAnnotation() {
        var self = this;

        this.registerEvents = function(campaignname) {
            // Manage submit button
            this.formContainer.querySelector("input[type='button']").addEventListener('click', (e) => {

                var eventfieldset = e.target.closest("fieldset"), valid = true;

                for (i = 0; i < eventfieldset.elements.length; i++) {
                    if (!eventfieldset.elements[i].checkValidity()) {
                        eventfieldset.elements[i].reportValidity();
                        valid = false;
                        break;
                    }
                }

                if (valid) {
                    var self = this;
                    makeCall("POST", 'WriteAnnotation', e.target.closest("form"),
                        function(req) {
                            if (req.readyState === XMLHttpRequest.DONE) {
                                var message = req.responseText; // error message or mission id
                                if (req.status === 200) {
                                    annotationForm.style.visibility = "hidden";
                                    annotationAlert.innerHTML = "";
                                    annotationAlert.textContent = "Annotation created successful";
                                    //todo controllare
                                    imageList.show(sessionStorage.getItem('campaignname'));

                                } else {
                                    notificatioMessage.textContent = message;

                                }
                            }
                        }
                    );
                }

            });

        };


    }
})();