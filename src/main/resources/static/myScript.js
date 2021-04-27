function validateForm() {
    let titleHolder = document.getElementById("title");
    let titleValidator = document.getElementById("title-info");

    if (!(titleHolder.value.length > 0)) {
        titleValidator.innerHTML = "title cannot be empty";
        return false;
    } else if(titleHolder.value.length > 20) {
        titleValidator.innerHTML = "title can contain max 20 char";
        return false;
    } else {
        titleValidator.innerHTML = "";
    }
}