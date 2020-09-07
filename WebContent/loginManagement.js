
/**
 * Login management
 */

// avoid variables ending up in the global scope
(function() {
	document.getElementById("loginbutton").addEventListener('click', (e) => {
	    let form = e.target.closest("form");
		if (form.checkValidity()) {
			makeCall("POST", 'CheckLogin', e.target.closest("form"),function(req) {
				if (req.readyState === XMLHttpRequest.DONE) {
					let message = req.responseText;
					if (req.status === 200) {
                        let user = JSON.parse(req.responseText);
						sessionStorage.setItem('user', req.responseText);
						if (user.role.includes("Manager")) {
						  window.location.href = "managerHome.html";
						} else {
						  window.location.href = "workerHome.html";
						}
					} else {
						document.getElementById("errormessage").innerHTML = "";
						document.getElementById("errormessage").textContent = message;
					}
				}
			});
		} else {
			 form.reportValidity();
		}
  });
  document.getElementById("registerbutton").addEventListener('click', (e) => {window.location.href = "register.html";});

})();
