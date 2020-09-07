/**
 * Login management
 */

(function (newChild) { // avoid variables ending up in the global scope

  const roleSelection = document.getElementById("RoleSelection");
  const registerForm = document.getElementById("RegisterForm");
  const managerButton = document.getElementById("managerButton");
  const workerButton = document.getElementById("workerButton");
  const insertToFieldset = document.getElementById("forLavoratore");
  var errorMessage = document.getElementById("errormessage");
  const roleTag  = document.getElementsByName("role");



  const pageOrchestrator = new PageOrchestrator();
  const role = new Role();
  const register = new Register();
  roleSelection.style.visibility = "hidden";
  registerForm.style.visibility = "hidden";



  window.addEventListener("load", () => {
        pageOrchestrator.start(); // initialize the components
        pageOrchestrator.refresh(); // display initial content
  }, false);

  function PageOrchestrator(){
      this.start = function(){
          role.show();
      }

      this.refresh = function(){

      }
  }

  function Role(){
        var self = this;
        this.show = function () {
              roleSelection.style.visibility = "visible";
              managerButton.addEventListener("click", (e) => {
                self.update("manager");
              }, false);
              workerButton.addEventListener("click", (e) => {
                self.update("worker");
              }, false);
        }


        this.update = function (roleName){
            roleSelection.style.visibility = "hidden";
            if(roleName === "manager"){
                  roleTag[0].value = "Manager";
            }else {
                roleTag[0].value = "Lavoratore";

                let lavoratoreLevel = document.createElement("label");
                lavoratoreLevel.textContent = "LavoratoreLevel:";
                insertToFieldset.appendChild(lavoratoreLevel);
                let select = document.createElement("select");
                select.required = true
                select.name = "lavoratoreLevel";
                let optionBassa = document.createElement("option");
                optionBassa.value = "basso";
                optionBassa.textContent = "Basso";
                let optionMedia = document.createElement("option");
                optionMedia.value = "medio";
                optionMedia.textContent = "Medio";
                let optionAlto = document.createElement("option");
                optionAlto.value = "alto";
                optionAlto.textContent = "Alto";
                select.appendChild(optionBassa);
                select.appendChild(optionMedia);
                select.appendChild(optionAlto);
                insertToFieldset.appendChild(select);

                let imageLabel = document.createElement("label");
                imageLabel.textContent = "Image:";
                insertToFieldset.appendChild(imageLabel);
                let image = document.createElement("input");
                image.type = 'file';
                image.name = 'image';
                image.required = true;
                imageLabel.appendChild(image);
            }
            register.show();
        }
  }


  function Register(){
      var self = this;
      this.show = function () {
          registerForm.style.visibility = "visible";
          document.getElementById("submitbutton").addEventListener('click', (e) => {

              let pwd = document.getElementsByName("pwd")[0].value;
              let confirmPwd = document.getElementsByName("confirmed")[0].value;
              if (pwd !== confirmPwd) {
                  errorMessage.textContent =
                        "Notice from client side: Password and confirm password must be the same.";
                  return;
              }
              let email = document.getElementsByName("email")[0].value;
              let re = new RegExp("^(\\S+)\\@(\\S+).(\\S+)$");
              if (!re.test(email)) {
                  errorMessage.textContent =
                        "Notice from client side: The syntactic of the email address is not valid.";
                  return;
              }

              var form = e.target.closest("form");

              if (form.checkValidity()) {
                  makeCall("POST", 'Register', e.target.closest("form"),function(req) {
                      if (req.readyState === XMLHttpRequest.DONE) {
                          var message = req.responseText;
                          if (req.status === 200) {
                            window.location.href = "loginPage.html";
                          } else {
                            errorMessage.innerHTML = "";
                            errorMessage.textContent = message;
                          }
                      }
                  });
              } else {
                form.reportValidity();
              }

          })
      }

      this.update = function (){

      }
  }

})();
