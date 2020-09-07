(function () { // avoid variables ending up in the global scope

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
                InsertForWorker(insertToFieldset);
            }
            register.show();
        }
  }

  function Register(){
      var self = this;
      this.show = function () {
          registerForm.style.visibility = "visible";
          submitInformationForm('Register',errorMessage,'loginPage.html');


      }

      this.update = function (){

      }
  }

})();
