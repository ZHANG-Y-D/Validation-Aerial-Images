(function(){

    var userName = document.getElementById("name");
    var email = document.getElementById("email");
    var level = document.getElementById("level");
    var image = document.getElementById("image");
    var errorMessage = document.getElementById("errorMessage");

    var pageOrchestrator = new PageOrchestrator();
    var profile = new Profile();
    var user;

    window.addEventListener("load", () => {
        pageOrchestrator.start(); // initialize the components
        pageOrchestrator.refresh(); // display initial content
    }, false);

    function PageOrchestrator(){

        this.start = function(){
            profile.show();
        }

        this.refresh = function() {


        }
    }

    function Profile(){
        var self = this;
        this.show = function (){
            self.update(getSession("user",errorMessage));
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





})();