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

            if(images.length === 0){
                annotationAlert.innerHTML = "";
                annotationAlert.textContent = "There is no image you need to comment on！";
            }
            images.forEach(image => {
                var imageLI = document.createElement("li")
                imageUL.appendChild(imageLI);
                var anchor = document.createElement("a");
                imageLI.appendChild(anchor);

                var imageTag = document.createElement("img");

                imageTag.src = "data:image/png;base64," + image.foto;
                imageTag.width = 150;
                anchor.appendChild(imageTag);
                anchor.setAttribute('id', image.id);
                anchor.addEventListener("click", (e) => {

                    annotationForm.style.visibility = "visible";
                    let imageId = e.currentTarget.getAttribute('id');
                    submitAnnotation.registerEvents(parseInt(imageId));

                }, false);
            });

        }
    }

    function SubmitAnnotation() {
        var self = this;


        this.registerEvents = function(imageId) {
            // Manage submit button

            // this.formContainer.querySelector("input[type='button']").addEventListener('click', (e) => {
            document.getElementById("create").addEventListener('click', (e) => {

                document.getElementsByName("imageId")[0].value = imageId;
                var form = e.target.closest("form");

                if (form.checkValidity()) {
                    makeCall("POST", 'writeAnnotation', e.target.closest("form"),function(req) {
                        if (req.readyState === XMLHttpRequest.DONE) {
                            var message = req.responseText;
                            if (req.status === 200) {
                                window.location.href = "writeAnnotation.html";
                                annotationAlert.textContent = "Annotation created successful";
                            } else {
                                notificatioMessage.innerHTML = "";
                                notificatioMessage.textContent = message;
                            }
                        }
                    });
                } else {
                    form.reportValidity();
                }


                // var eventfieldset = e.target.closest("fieldset");
                // var valid = true;
                //
                // for (let i = 0; i < eventfieldset.elements.length; i++) {
                //     if (!eventfieldset.elements[i].checkValidity()) {
                //         eventfieldset.elements[i].reportValidity();
                //         valid = false;
                //         break;
                //     }
                // }
                //
                // if (valid) {
                //     makeCall("POST", 'writeAnnotation', e.target.closest("form"),
                //         function(req) {
                //             if (req.readyState === XMLHttpRequest.DONE) {
                //                 var message = req.responseText; // error message or mission id
                //                 if (req.status === 200) {
                //                     window.location.href = "writeAnnotation.html";
                //                     annotationForm.style.visibility = "hidden";
                //                     annotationAlert.innerHTML = "";
                //                     annotationAlert.textContent = "Annotation created successful";
                //
                //                 } else {
                //                     notificatioMessage.textContent = message;
                //                 }
                //
                //             }
                //     });
                //
                // }

            });

        };


    }
        

})();