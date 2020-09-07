(function(){

    var imageDiv = document.getElementById("imagesDiv");
    var annotationForm = document.getElementById("annotationForm");
    annotationForm.style.visibility = "hidden";
    var notificatioMessage = document.getElementById("notificationMessage");
    var annotationAlert = document.getElementById("annotationAlert");

    var pageOrchestrator = new PageOrchestrator();

    var imageList
    var submitAnnotation;
    var campaignName


    window.addEventListener("load", () => {
        pageOrchestrator.start(); // initialize the components
    }, false);

    function PageOrchestrator(){
        this.start = function (){
            imageList = new ImageList();
            submitAnnotation = new SubmitAnnotation();

            imageList.show();

        }

    }


    function ImageList() {
        var self = this;

        this.show = function () {
            campaignName = getSession("CampaignName", annotationAlert)
            makeCall("GET", "GetNotAnnotatedImages?CampaignName=" + campaignName, null,
                function (req) {
                    if (req.readyState === 4) {
                        var message = req.responseText;
                        if (req.status === 200) {
                            //todo controllare
                            self.update(JSON.parse(req.responseText));

                        } else {
                            annotationAlert.innerHTML = "";
                            annotationAlert.textContent = message;
                        }
                    }
                });
        }

        this.update = function (images) {

            var imageUL = document.createElement("ul");
            imageDiv.appendChild(imageUL);

            images.forEach(image => {
                var imageLI = document.createElement("li")
                imageUL.appendChild(imageLI);
                var anchor = document.createElement("a");
                imageLI.appendChild(anchor);

                var imageTag = document.createElement("img");

                imageTag.src = "data:image/png;base64," + image.foto;
                //todo da mettere con CSS
                imageTag.width = 150;
                imageTag.height = 150;
                anchor.appendChild(imageTag);

                anchor.setAttribute('id', image.id);

                anchor.addEventListener("click", (e) => {

                    annotationForm.imageId.value = e.target.getAttribute("id");
                    //annotationForm.annotationDate.value = new Date();
                    annotationForm.style.visibility = "visible";
                    submitAnnotation.registerEvents();
                }, false);


            });
        }

    }

    function SubmitAnnotation() {
        var self = this;


        this.registerEvents = function() {
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
                        makeCall("POST", 'writeAnnotation', e.target.closest("form"),
                            function(req) {
                                if (req.readyState === XMLHttpRequest.DONE) {
                                    var message = req.responseText; // error message or mission id
                                    if (req.status === 200) {
                                        window.location.href = "writeAnnotation.html";
                                        annotationForm.style.visibility = "hidden";
                                        annotationAlert.innerHTML = "";
                                        annotationAlert.textContent = "Annotation created successful";
                                        //todo controllare
                                        //imageList.show(sessionStorage.getItem('campaignname'));

                                    } else {
                                        notificatioMessage.textContent = message;

                                    }

                                }
                            });

                }

            });

        };


    }
        

})();