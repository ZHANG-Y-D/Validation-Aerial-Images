/**
 * AJAX call management
 */

function makeCall(method, url, formElement, cback, reset = true) {
	var req = new XMLHttpRequest(); // visible by closure
	req.onreadystatechange = function() {
	  cback(req)
	}; // closure
	req.open(method, url);
	if (formElement == null) {
	  req.send();
	} else {
	  req.send(new FormData(formElement));
	}
	if (formElement !== null && reset === true) {
	  formElement.reset();
	}

}

function getSession(key, messageTag){
	let value;

	if (typeof (Storage) !== "undefined") {
		value = JSON.parse(sessionStorage.getItem(key));

	} else {
		messageTag.innerHTML = "";
		messageTag.innerHTML = "Sorry, your browser does not support Web Storage...";
	}

	if (value == null || value === ''){
		alert("Session value not found!");
	}

	return value;
}

function ShowUserProfile(userNameTag, messageDiv){
	let user = getSession("user",messageDiv)
	userNameTag.textContent = user.username;
	userNameTag.addEventListener("click", (e) => {
		window.location.href = "userProfile.html";
	}, false);

}
