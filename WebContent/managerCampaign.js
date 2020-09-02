(function(){

    var campainListDiv = document.getElementById("campainList");
    var pageOrchestrator = new PageOrchestrator();

    var campainList;
    var createCampaign;

    window.addEventListener("load", () => {
          pageOrchestrator.start(); // initialize the components
          pageOrchestrator.refresh(); // display initial content
      }, false);


    function PageOrchestrator(){

        this.start = function(){
            campainList = new CampainList();
            createCampaign = new CreateCampaign();

            campainList.show();

        }

    }



    function CampainList(){

        var self = this;
        this.show =  function () {
            makeCall("GET", "GetCampaignList", null,
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

        this.update = function (campainList){
            if(campainList.length == 0){
                campainListDiv.innerHTML = "";
                campainListDiv.textContent = "No campaign yet!";
            }else {

                var list = document.createElement("ul");
                campainListDiv.appendChild("list");

                campainList.forEach(campaign => {

                  var li = document.createElement("li");
                  list.appendChild(li);
                  var anchor = document.createElement("a");
                  li.appendChild(anchor);
                  anchor.textContent = campaign.name;
                  anchor.setAttribute('campaignName', campaign.name);

                  anchor.addEventListener("click", (e) => {
                      //todo reindrizzamento alla pagina dettagli

                      // .show(e.target.getAttribute("campaignName"));
                  }, false);
                 // anchor.href = "#";
                });

            }

        }


    }

    function CreateCampaign(){

    }


})();
