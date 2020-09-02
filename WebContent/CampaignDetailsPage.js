(function() { // avoid variables ending up in the global scope


    var contentDiv = document.getElementById("contentDiv");
    var messageDiv = document.getElementById("messageDiv");

    var downloadImage = new DownloadImage();
    var submitImage = new SubmitImage();
    var pageOrchestrator = new PageOrchestrator();

    window.addEventListener("load", () => {
        pageOrchestrator.start(); // initialize the components
        pageOrchestrator.refresh(); // display initial content
    }, false);

    function PageOrchestrator(){

        this.start = function(){
            downloadImage.show();
            submitImage.show();
        }

        this.refresh = function(){

        }

    }

    function DownloadImage(){
        var self = this;

        this.show = function () {
            makeCall("GET", "DownloadImage", null,
                function (req) {
                    if (req.readyState === 4) {
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
                messageDiv.textContent = "No folders yet!";
            } else {
                var row = document.createElement("div");
                row.textContent = imageList[0].campagnaName;
                contentDiv.appendChild(row)
                var imageUL = document.createElement("ul");
                contentDiv.appendChild(imageUL);
                imageList.forEach( image => {
                    var imageLI = document.createElement("li")
                    imageUL.appendChild(imageLI);
                    var imageTag = document.createElement("img");

                    imageTag.src = "data:image/png;base64,"+image.foto;
                    imageTag.width = 300;
                    imageTag.height = 300;
                    imageLI.appendChild(imageTag);
                });
            }

        }

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
                                switch (req.status) {
                                    case 200:
                                        // sessionStorage.setItem('username', message);
                                        window.location.href = "CampaignDetailsPage.html";
                                        break;
                                    case 400: // bad request
                                        messageDiv.textContent = message;
                                        break;
                                    case 401: // unauthorized
                                        messageDiv.textContent = message;
                                        break;
                                    case 500: // server error
                                        messageDiv.textContent = message;
                                        break;
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