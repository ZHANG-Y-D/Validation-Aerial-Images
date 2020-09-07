(function(){

    var userName = document.getElementById("name");
    var email = document.getElementById("email");
    var level = document.getElementById("level");
    var image = document.getElementById("image");
    var errorMessage = document.getElementById("errorMessage");
    const informationForm = document.getElementById("informationForm");
    const insertToFieldset = document.getElementById("forLavoratore");
    const modifyTag = document.getElementById("modify");

    var pageOrchestrator = new PageOrchestrator();
    var profile = new Profile();
    var modifyProfile = new ModifyProfile();
    var user;

    window.addEventListener("load", () => {
        pageOrchestrator.start(); // initialize the components
        pageOrchestrator.refresh(); // display initial content
    }, false);

    function PageOrchestrator(){

        this.start = function(){
            profile.show();
            modifyProfile.show();
        }

        this.refresh = function() {


        }
    }

    function Profile(){
        var self = this;
        this.show = function (){
            user = getSession("user",errorMessage);
            self.update(user);
        }

        this.update = function (user){
            userName.textContent = "Name: " + user.username;
            email.textContent = "E-mail: " + user.email;
            if (user.role === 'Manager'){
                level.style.visibility="hidden";
                image.style.visibility="hidden";
            } else {
                level.textContent = "Level: " + user.level;
                image.src = "data:image/png;base64,"+user.foto;
                image.width = 300;
            }

        }
    }

    function ModifyProfile(){
        let self = this;
        this.show = function(){
            informationForm.style.visibility = "hidden";
            modifyTag.addEventListener("click",ev => {
                informationForm.style.visibility = "visible";
                self.update();
            });
        }

        this.update = function() {
            if (user.role === 'Lavoratore'){
                InsertForWorker(insertToFieldset);
            }
            submitInformationForm('UpdateProfile',errorMessage,'userProfile.html');

        }
    }





})();