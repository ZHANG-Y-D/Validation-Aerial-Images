(function(){

    var messageDiv = document.getElementById("messageDiv");
    var annotationDiv = document.getElementById("annotationDiv");
    var annotationContainer = document.getElementById("annotationContainer");
    var annotationAlert = document.getElementById("annotationAlert");
    var workerInformationDiv = document.getElementById("workerInformationDiv");
    workerInformationDiv.style.visibility = "hidden";
    var pageOrchestrator = new PageOrchestrator();
    var imageDetails ,annotationList,workerDetails;


    window.addEventListener("load", () => {

        if (typeof (Storage) !== "undefined") {
            pageOrchestrator.start(getSession("ImageId",messageDiv)); // initialize the components
            pageOrchestrator.refresh(); // display initial content
        }else {
            messageDiv.innerHTML = "Sorry, your browser does not support Web Storage...";
        }

    }, false);


    function PageOrchestrator(){
        this.start = function (imageId){
            imageDetails = new ImageDetails({
                foto: document.getElementById("foto"),
                nomeCampagna: document.getElementById("nomeCampagna"),
                x: document.getElementById("x"),
                y: document.getElementById("y"),
                regione: document.getElementById("regione"),
                comune: document.getElementById("comune"),
                provenienza: document.getElementById("provenienza"),
                dataRecupero: document.getElementById("dataRecupero"),
                risoluzione: document.getElementById("risoluzione")
            });
            imageDetails.show(imageId);
            annotationList = new AnnotationList();
            annotationList.show(imageId);

            workerDetails = new WorkerDetails({
                lavoratoreFoto : document.getElementById("lavoratoreFoto"),
                lavoratoreName : document.getElementById("lavoratoreName"),
                esperienza: document.getElementById("esperienza"),
                email: document.getElementById("lavoratoreEmail")

            });

        }

        this.refresh = function (){

        }




    }

    function ImageDetails(options){
        var self = this;

        this.foto = options['foto'],
        this.nomeCampagna =  options['nomeCampagna'],
        this.x = options['x'];
        this.y = options['y'];
        this.regione = options['regione'];
        this.comune = options['comune'];
        this.provenienza = options['provenienza'];
        this.dataRecupero = options['dataRecupero'];
        this.risoluzione = options['risoluzione'];

        this.show = function (imageId){
            makeCall("GET", "GetImageDetails?imageId="+imageId, null,function (req) {
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

        this.update = function (image){
            self.foto.src = "data:image/png;base64,"+image.foto;
            //todo fare in CSS
            self.foto.width = 300;
            self.foto.height = 300;
            self.nomeCampagna.textContent = image.campagnaName;
            self.x.textContent = image.latitude;
            self.y.textContent= image.latitude;
            self.regione.textContent= image.regione;
            self.provenienza.textContent= image.provenienza;
            self.risoluzione.textContent = image.risoluzione;
            self.dataRecupero.textContent = image.date;
            self.comune.textContent = image.comune;

        }




    }

    function AnnotationList(){
        var self = this;

        this.show = function (imageId){
            makeCall("GET", "GetAnnotations?imageId="+imageId, null,function (req) {
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

        this.update = function (listAnnotation){
            if(listAnnotation.length === 0){
                annotationDiv.style.visibility = "hidden";
                annotationAlert.innerHTML = "";
                annotationAlert.textContent = "No annotation yet";
            }else {
                annotationDiv.style.visibility = "visible";
                annotationContainer.innerHTML = "";

                listAnnotation.forEach(function (annotation){
                    row = document.createElement("tr");
                    lavoratoreCell = document.createElement("td")
                    row.appendChild(lavoratoreCell);

                    anchor = document.createElement("a");
                    anchor.textContent = annotation.lavoratoreName;
                    lavoratoreCell.appendChild(anchor);
                    anchor.setAttribute('workerName', annotation.lavoratoreName); // set a custom HTML attribute
                    anchor.addEventListener("click", (e) => {
                        // dependency via module parameter
                        workerDetails.show(e.target.getAttribute("workerName")); // the list must know the details container
                    }, false);
                    anchor.href = "#";


                    dataCell = document.createElement("td");
                    dataCell.textContent = annotation.dataCreazione;
                    row.appendChild(dataCell);
                    fiduciaCell =  document.createElement("td");
                    fiduciaCell.textContent = annotation.fiducia;
                    row.appendChild(fiduciaCell);
                    validitaCell = document.createElement("td");
                    validitaCell.textContent = annotation.validita;
                    row.appendChild(validitaCell);
                    noteCell = document.createElement("td");
                    noteCell.textContent = annotation.note;

                    annotationContainer.appendChild(row);


                });
            }

        }

    }

    function WorkerDetails(options){
        var self = this;
        this.lavoratoreFoto = options['lavoratoreFoto'];
        this.lavoratoreName = options['lavoratoreName'];
        this.esperienza = options['esperienza'];
        this.email = options['email'];

        this.show = function (workerName){
            makeCall("GET", "GetUserDetails?userName="+workerName, null,function (req) {
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

        this.update = function (worker){
            workerInformationDiv.style.visibility = "visible";
            //todo foto
            self.lavoratoreFoto.src = "data:image/png;base64,"+worker.foto;
            self.lavoratoreFoto.width = 150;
            self.lavoratoreFoto.height = 150;
            self.lavoratoreName.textContent = worker.username;
            self.esperienza.textContent = worker.level;
            self.email.textContent = worker.email;

        }



    }



})();