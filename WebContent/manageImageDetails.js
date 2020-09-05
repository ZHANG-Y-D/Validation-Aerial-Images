(function(){

    var pageOrchestrator = new PageOrchestrator();

    var imageDetails , workerDetails;
    //TODO da controllare ID
    var imageId = sessionStorage.getItem("ImageId");

    window.addEventListener("load", () => {
        pageOrchestrator.start(); // initialize the components
        pageOrchestrator.refresh(); // display initial content
    }, false);


    function PageOrchestrator(){
        imageDetails = new ImageDetails({
            x: document.getElementById("x"),
            y: document.getElementById("y"),
            regione: document.getElementById("regione"),
            comune: document.getElementById("comune"),
            provenienza: document.getElementById("provenienza"),
            dataRecupero: document.getElementById("dataRecupero"),
            risoluzione: document.getElementById("risoluzione")
        });
        imageDetails.show();



    }

    function ImageDetails(options){
        this.x = options['x'];
        this.y = options['y'];
        this.regione = options['regione'];
        this.comune = options['comune'];
        this.provenienza = options['provenienza'];
        this.dataRecupero = options['dataRecupero'];
        this.risoluzione = options['risoluzione'];

        this.show = function (){
            
        }




    }

    function WorkerDetails(){

    }



})();