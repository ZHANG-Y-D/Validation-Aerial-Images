(function() {
    var detailsDiv = document.getElementById("detailsDiv");
    var messageDiv = document.getElementById("statisticsAlert");
    var campaignName = document.getElementById("campaignName");
    var totalImage = document.getElementById("totalImage");
    var totalAnnotation= document.getElementById("totalAnnotation");
    var average = document.getElementById("average");
    var totalConflicts = document.getElementById("totalConflicts");

    var statistics = new Statistics();
    statistics.show();


    function Statistics(){
        var self = this;
        this.show =  function () {
            makeCall("GET", "GetStatisticsJS", null,
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

        this.update = function (numberList){
            if (typeof (Storage) !== "undefined") {
                campaignname = sessionStorage.getItem("CampaignName");
                campaignName.textContent = campaignname;
            } else {
                messageDiv.innerHTML = "";
                messageDiv.innerHTML = "Sorry, your browser does not support Web Storage...";
            }

            if (campaignname == null || campaignname === ''){
                alert("Campaign not found!");
            }

            totalImage.textContent = numberList[0];
            totalAnnotation.textContent = numberList[1];
            average.textContent = numberList[2];
            totalConflicts.textContent = numberList[3];

        }

    }

})();