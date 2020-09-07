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

function InsertForWorker(insertToFieldset){
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

function submitInformationForm(url,errorMessage, toUrl){
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
			makeCall("POST", url, e.target.closest("form"),function(req) {
				if (req.readyState === XMLHttpRequest.DONE) {
					var message = req.responseText;
					if (req.status === 200) {
						sessionStorage.setItem('user', req.responseText);
						window.location.href = toUrl;
					} else {
						errorMessage.innerHTML = "";
						errorMessage.textContent = message;
						return false;
					}
				}
			});
		} else {
			form.reportValidity();
		}

	});
}