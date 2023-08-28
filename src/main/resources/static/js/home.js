
function searchProducts() {
    document.querySelector(".search-input").setAttribute("style", "display:block")
}
function handleRemovesearch(){
    document.querySelector(".search-input").removeAttribute("style")
    document.querySelector("#ulSearch").removeAttribute("style")
}

let input = document.querySelector("#searchProducts")
input.onkeyup = async function () {
    const option = {
        method: "post",
        header: {'Content-Type': 'application/json'},
        body: JSON.stringify({value: input.value})
    }
    let ul = document.querySelector("#ulSearch")
    ul.length == 0 && ul.setAttribute("style", "display:none")
    await fetch('http://localhost:8080/product/search/posts', option).then((res) => res.json()).then(data => {
        ul.innerHTML = ""
        data.length != 0 ? ul.setAttribute("style", "display:block") : ul.setAttribute("style", "display:none")
        data.forEach(item => {
            ul.innerHTML += `<li onclick="submitForm('${item}')">${item}</li>`
        })

    });
};
function showProduct(val){
    let prev = val.getAttribute("src")
    let current =  val.parentElement.previousSibling.previousSibling.children[0].children[0].getAttribute("src")
    val.parentElement.previousSibling.previousSibling.children[0].children[0].setAttribute("src",prev)

console.log(val.parentElement.previousSibling.previousSibling.children[0].children[0].getAttribute("src"))
console.log(val)
    val.setAttribute("src", current);}
function submitForm(val) {
    document.querySelector("#searchProducts").value = val
    document.querySelector("#searchProducts").form.submit()
}