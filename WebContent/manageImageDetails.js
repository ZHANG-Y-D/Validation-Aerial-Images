(function(){

    var messageDiv = document.getElementById("messageDiv");
    var annotationDiv = document.getElementById("annotationDiv");
    var annotationContainer = document.getElementById("annotationContainer");
    var annotationAlert = document.getElementById("annotationAlert");
    var pageOrchestrator = new PageOrchestrator();

    var imageDetails ,annotationList,workerDetails;

    //TODO da controllare ID


    window.addEventListener("load", () => {

        if (typeof (Storage) !== "undefined") {
            var imageId = sessionStorage.getItem("ImageId");
            pageOrchestrator.start(imageId); // initialize the components
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
            //todo foto
           // self.foto.textContent = image.foto;
            self.nomeCampagna.textContent = image.campagnaName;
            self.x.textContent = image.latitude;
            self.y.textContent= image.latitude;
            self.regione.textContent= image.regione;
            self.provenienza.textContent= image.provenienza;
            self.risoluzione.textContent = image.risoluzione;
            self.dataRecupero.textContent = image.dataRecupero;
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
                    lavoratoreCell = document.createElement("td");
                    lavoratoreCell.textContent = annotation.lavoratoreName;
                    row.appendChild(lavoratoreCell);
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

    function WorkerDetails(){

    }



})();