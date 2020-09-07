(function(){

    var userName = document.getElementById("username");
    var campaignListDiv = document.getElementById("campaignListDiv");
    var formErrorMessage = document.getElementById("formErrorMessage");
    var messageDiv = document.getElementById("messageDiv");

    var pageOrchestrator = new PageOrchestrator();
    var campaignList;
    var createCampaign;


    window.addEventListener("load", () => {
          pageOrchestrator.start(); // initialize the components
          pageOrchestrator.refresh(); // display initial content
      }, false);


    function PageOrchestrator(){

        this.start = function(){

            ShowUserProfile(userName, messageDiv);
            campaignList = new CampaignList();
            createCampaign = new CreateCampaign(document.getElementById("campaignForm"));
            campaignList.show();
            createCampaign.registerEvents(this);

        }

        this.refresh = function() {

        }

    }

    function CampaignList(){

        var self = this;
        this.show =  function () {
            makeCall("GET", "GetCampaignList", null,
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

        this.update = function (campaignList){
            if(campaignList.length === 0){
                campaignListDiv.innerHTML = "";
                campaignListDiv.textContent = "No campaign yet!";
            }else {
                var title = document.createElement("p");
                title.textContent = "Your campaigns:"
                campaignListDiv.appendChild(title);
                var list = document.createElement("ul");
                campaignListDiv.appendChild(list);

                campaignList.forEach(campaign => {

                  var li = document.createElement("li");
                  list.appendChild(li);
                  var anchor = document.createElement("a");
                  li.appendChild(anchor);
                  anchor.textContent = campaign.name;
                  anchor.setAttribute('campaignName', campaign.name);
                  anchor.addEventListener("click", (e) => {

                      sessionStorage.setItem("CampaignName",JSON.stringify(e.target.getAttribute("campaignName")))
                      window.location.href = "CampaignDetailsPage.html";


                  }, false);
                 // anchor.href = "#";
                });

            }

        }


    }

    function CreateCampaign(formId){

        this.formContainer = formId;
        this.registerEvents = function(orchestrator) {
            // Manage submit button
            this.formContainer.querySelector("input[type='button']").addEventListener('click', (e) => {

                var eventfieldset = e.target.closest("fieldset"), valid = true;
                let createdCampaignName = document.getElementsByName("name")[0].value

                for (i = 0; i < eventfieldset.elements.length; i++) {
                    if (!eventfieldset.elements[i].checkValidity()) {
                      eventfieldset.elements[i].reportValidity();
                      valid = false;
                      break;
                    }
                }

                if (valid) {
                    var self = this;
                    makeCall("POST", 'CreateCampaign', e.target.closest("form"),function(req) {
                        if (req.readyState === XMLHttpRequest.DONE) {
                            var message = req.responseText; // error message or mission id
                            if (req.status === 200) {
                                sessionStorage.setItem("CampaignName",JSON.stringify(createdCampaignName));
                                window.location.href = "CampaignDetailsPage.html";
                            } else {
                                formErrorMessage.textContent = message;

                            }
                        }
                      }
                    );
                }

          });

      };

    }


})();
