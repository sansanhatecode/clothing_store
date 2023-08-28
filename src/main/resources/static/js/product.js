// handle show productImgColor
const productImgDetail = () => {
    document.querySelector("#tb2").classList.add("table2")
    document.querySelector("#tb1").classList.add("table1")
    document.querySelector("#tb2").classList.remove("tabledefault")
    document.querySelector("#tb1").classList.remove("tabledefault")
}
// handle show product
const product = () => {
    document.querySelector("#tb2").classList.add("tabledefault")
    document.querySelector("#tb1").classList.add("tabledefault")
    document.querySelector("#tb2").classList.remove("table2")
    document.querySelector("#tb1").classList.remove("table1")
}
const Addrecord = () => {
    document.querySelector("#createForm").setAttribute("style", "display : block")
    document.querySelector("#updateProduct").removeAttribute("style")
    document.querySelector("#ProductID").setAttribute("style", "")

    handle.classList.toggle("handle")
}
const Addrecord2 = async () => {
    let idprd = document.querySelector("#ProductIDList").value
    await ListProduct(idprd)
    queryListProductColorByColorId()
    document.querySelector("#IdProduct").children[1].setAttribute("value", idprd)
    document.querySelector("#IdProduct").children[1].innerHTML = idprd
    document.querySelector("#createButtonProduct").setAttribute("style", "display : block");
    document.querySelector("#updateButtonProduct").setAttribute("style", "display : none");
    document.querySelector("#ProductIDList").removeAttribute("style")
    handle2.classList.toggle("handle2")

}
const Addrecord3 = () => {
    document.querySelector("#createButtonProductColor").setAttribute("style", "display : block");
    handle4.classList.toggle("handle4")

}
window.addEventListener("load", () => {
    location.href.includes("http://localhost:8080/admin/product?type=category&size=5") || localStorage.removeItem("list")
    let listProduct = document.getElementsByName("listProductByCategory")
    let listProductChecked = []
    if (localStorage.getItem("list") != null) {
        listProductChecked = localStorage.getItem("list").split(",")
    }
    listProduct.forEach(item => {
        listProductChecked.forEach(itemcheck => {
            if (item.value == itemcheck) {
                item.checked = true
            }
        })
    })
})
//handle toogle form add product
const showFormFilter = () => {
    handle3.classList.add("handle3")
}
const handleDeleteProductImg = (val) => {
   let id = val.parentElement.parentElement.parentElement.parentElement.parentElement.children[0].getAttribute("placeholder")
    val.setAttribute("href", `/admin/product/deleteProductImgAndColor?productImgId='+${id}`)
    productImgDetail()
}
// handle call Form add product
const createProduct = () => {
    let productId = document.querySelector("#ProductID")
    let productName = document.querySelector("#productName")
    let createDate = document.querySelector("#createdDate")
    let price = document.querySelector("#viewcount")
    let desc = document.querySelector("#description")
    let avtImg = document.querySelector("#avt")
    let allColumns = [productId, productName, createDate, price, desc, avtImg]
    let a = true
    // kiểm tra dữ liệu nhập vào
    allColumns.every(item => {
        switch (item.value) {
            case "":
                message(`Bạn không thể thêm sản phẩm nếu thiếu ${item.getAttribute("placeholder")}`)
                a = false
                break;
            case avtImg.value:
                if (item.getAttribute("src") == "") {
                    message(`Bạn không thể thêm sản phẩm nếu thiếu ${item.getAttribute("class")} `)
                    a = false;
                }
                break;
            case price.value:
                if (/^\d+$/.test(item.value) == false) {
                    message(`Giá sản phẩm phải là 1 số nguyên dương`)
                    a = false
                }
                break;
            case createDate.value:
                switch (Date.parse(createDate.value)) {
                    case NaN:
                        message(`Ngày thêm sản phẩm không đúng định dạng `)
                        a = false
                        break;
                    default:
                        if (new Date(createDate.value) > new Date()) {
                            message(`Ngày thêm sản phẩm không được lớn hơn ngày hiện tại`)
                            a = false
                        }
                        break;
                }
                break;
            default :
                break;
        }
        return a
    })
    if (a == true) {
        $('#submit').prop('action', '/admin/product/create')
        $("#submit").submit()
    }
}

function message(value) {
    document.querySelector(".toast-body").innerHTML = value
    const status = document.querySelector("#status")
    status.classList.add("done_delete")
    setTimeout(() => status.classList.remove("done_delete"), 4000)
}

//handle call form add product color and product img
const createImgProduct = () => {
    //chưa kiểm tra đầu vào
    $('#submitImgAndColor').prop('action', '/admin/product/createImgProduct');
    $('#submitImgAndColor').submit();

}
const createColorProduct = () => {
    let avaible = document.querySelector("#avaible").value
    if (avaible != null && parseInt(avaible) != NaN && avaible > 0) {
        $('#submitColor').prop('action', '/admin/product/createColorProduct');
        $('#submitColor').submit();
    } else {
        document.querySelector(".toast-body").innerHTML = "Số lượng bạn nhập không hợp lệ"
        const status = document.querySelector("#status")
        status.classList.add("done_delete")
        setTimeout(() => status.classList.remove("done_delete"), 4000)
    }
}

const updateImgProduct = () => {

    if (document.querySelector("#avt2").getAttribute("src") != null) {
        $('#submitImgAndColor').prop('action', '/admin/product/updateProductImg');
        $('#submitImgAndColor').submit();
    }
}
const updateColorProduct = () => {
    $('#submitProductColor').prop('action', '/admin/product/updateProductColor');
    $('#submitProductColor').submit();
}
//handle call update product
const updateForm = () => {
    let productId = document.querySelector("#ProductID")
    let productName = document.querySelector("#productName")
    let createDate = document.querySelector("#createdDate")
    let price = document.querySelector("#viewcount")
    let desc = document.querySelector("#description")
    let avtImg = document.querySelector("#avt")
    let allColumns = [productId, productName, createDate, price, desc, avtImg]
    let a = true
    // kiểm tra dữ liệu nhập vào
    allColumns.every(item => {
        switch (item.value) {
            case "":
                message(`Bạn không thể chỉnh sửa sản phẩm nếu thiếu ${item.getAttribute("placeholder")}`)
                a = false
                break;
            case avtImg.value:
                if (item.getAttribute("src") == "") {
                    message(`Bạn không thể chỉnh sửa sản phẩm nếu thiếu ${item.getAttribute("class")} `)
                    a = false;
                }
                break;
            case price.value:
                if (/^\d+$/.test(item.value) == false) {
                    message(`Giá sản phẩm phải là 1 số nguyên dương`)
                    a = false
                }
                break;
            case createDate.value:
                switch (Date.parse(createDate.value)) {
                    case NaN:
                        message(`Ngày thêm sản phẩm không đúng định dạng `)
                        a = false
                        break;
                    default:
                        if (new Date(createDate.value) > new Date()) {
                            message(`Ngày thêm sản phẩm không được lớn hơn ngày hiện tại`)
                            a = false
                        }
                        break;
                }
                break;
            default :
                break;
        }
        return a
    })
    if (a) {
        $('#submit').prop('action', '/admin/product/update');
        $("#submit").submit();
    }
}
const productCate = () => {
    $('#listProductByCategory').prop('action', '/admin/product?type=category&size=5&page=1');
    $("#listProductByCategory").submit();
}
//edit productImg form
const editProductImg = async (thisVal) => {
    let imgID = thisVal.getAttribute("placeholder")
    let tdList = thisVal.parentElement.parentElement.children
    let srcImg = tdList[5].children[0].getAttribute("src")
    let IdProduct = tdList[0].innerHTML
    tdList[0].setAttribute("value", IdProduct)
    let idColor = tdList[4].getAttribute("class")
    document.querySelector("#avt2").setAttribute("src", srcImg)
    document.querySelector("#imgidprd").setAttribute("value", tdList[5].getAttribute("id"))
    await submitlistProduct(IdProduct)
    let ProductIDList = document.querySelector("#ProductIDList").children
    for (idx = 0; idx < ProductIDList.length; idx++) {
        ProductIDList[idx].removeAttribute("selected")
    }
    document.querySelector("#IdProduct").children[1].setAttribute("value", IdProduct)
    document.querySelector(`#colorID${idColor}`).setAttribute("selected", "selected")
    document.querySelector(`#option${IdProduct}`).setAttribute("selected", "selected");
    document.querySelector("#handle2").classList.add("handle2")
    document.querySelector("#updateButtonProduct").setAttribute("style", "display:block")
    document.querySelector("#createButtonProduct").setAttribute("style", "display:none")
    document.querySelector("#ProductIDList").setAttribute("style", "pointer-events:none;")
    // productImgDetail()
}

window.addEventListener("load", () => {
    location.href.includes("type=category") ? document.querySelector("#showFormFilter").setAttribute("style", "display:block") :
        document.querySelector("#showFormFilter").removeAttribute("style")
    location.href.includes("size2=") && productImgDetail()
    location.href.includes("/product") && document.querySelector("#productIcon").setAttribute("style", "color:#fff; scale:1.1")
    location.href === "http://localhost:8080/admin" && document.querySelector("#admin").setAttribute("style", "color:#fff; scale:1.1")
    let list = document.getElementsByName("listProductColor")
    let prevProductID = localStorage.getItem("currentProductID")
    list.forEach(item => {
        if (item.getAttribute("placeholder") == item.getAttribute("value")) {
            item.setAttribute("selected", "selected")
            localStorage.setItem("currentProductID", item.getAttribute("value"))
        }
    })
    let currentProductID = localStorage.getItem("currentProductID")
    if (prevProductID != currentProductID) {
        productImgDetail()
        Addrecord2()
    }

})

//handle delete message

async function submitlistProduct(item) {
    typeof item == "object" ? await ListProduct(item.value) : await ListProduct(item)
    queryListProductColorByColorId()
}

function queryListProductColorByColorId() {
    let list = JSON.parse(localStorage.getItem("listProductColorByProductId"))
    let idprd = document.querySelector("#ProductIDList").value
    let comboboxColor = document.querySelector("#ListColorProduct")
    let length = comboboxColor.children.length
    for (i = length - 1; i >= 0; i--) {
        comboboxColor.removeChild(comboboxColor.children[i])
    }
    list.forEach(item => {
        let node = document.createElement("option")
        node.setAttribute("value", item["colorID"])
        node.setAttribute("id", `colorID${item["colorID"]}`)
        node.innerHTML = item["color_name"]
        comboboxColor.appendChild(node)
    })
    document.querySelector("#IdProduct").children[1].setAttribute("value", idprd)
}

const getProductID = () => {
    //lấy product id cho table product color
    let currentProductId = document.querySelector("#ProductIDList1")
    let inputProductID = document.querySelector("#idPrd")
    inputProductID.setAttribute("value", currentProductId.value)
    document.querySelector(".prdIdInput").setAttribute("style", "margin-top: 54px; opacity: 1")
}

function demo(currentval) {
    let valueProduct = currentval.parentElement.parentElement
    document.querySelector("#ProductID").value = valueProduct.children[0].getAttribute("id")
    document.querySelector("#ProductID").setAttribute("style", "pointer-events:none;")
    document.querySelector("#productName").value = valueProduct.children[0].innerHTML
    document.querySelector("#createdDate").value = valueProduct.children[3].innerHTML.split("-").reverse().join("-")
    document.querySelector("#viewcount").value = valueProduct.children[1].innerHTML.replace(".0","")
    document.querySelector("#categoryid").value = valueProduct.children[4].getAttribute("alt")
    document.querySelector("#description").value = valueProduct.children[6].children[0].innerHTML
    document.querySelector("#avt").setAttribute("src", valueProduct.children[2].children[0].getAttribute("src"))
    document.querySelector("#handle").classList.add("handle")
    document.querySelector("#updateProduct").setAttribute("style", "display:block")
    document.querySelector("#createForm").removeAttribute("style")
}

async function showFormProductColor(val) {
    document.querySelector(".handle5") == null && setTimeout(async () => {
        let body = document.querySelector("body").getAttribute("class")
        if (document.querySelector(".handle") == null && (body == null || body === "")) {
            let currentVal = "#body" + val.children[0].getAttribute("id")
            let tbodyProductColorbyProductID = document.querySelector(currentVal)
            await ListProduct(val.children[0].getAttribute("id"))
            let listProductColorbyProductID = JSON.parse(localStorage.getItem("listProductColorByProductId"))
            let span = document.createElement("span")
            if (tbodyProductColorbyProductID.children.length == 0) {
                await listProductColorbyProductID.forEach(item => {
                    tbodyProductColorbyProductID.innerHTML += `<tr>
                                                <td class=${item["product"].productID}  >${item["product"].name}</td>
                                                <td>${item["color_name"]}</td>
                                                <td class=${item["colorhex"]} style="position: relative"> <span style=" position: absolute; height: 40px; width: 40px; bottom: 10px;border-radius: 50% ; left:65px; background-color: ${item["colorhex"]}"> </span></td>
                                                <td>${item["available"]}</td>
                                                <td> ${item["product"].deprecated == true ? 'Ngưng bán' : 'Đang bán'}</td>
                                                <td class=${item["colorID"]} id="${item["colorID"]}color" onclick="handleEditProductColor(this)" ><i class="fa-solid fa-pen-to-square" style="color: #212529"></i></td>
                                                <td>
                                                <!-- Button trigger modal -->
                                                    <button class="btn" data-target="#exampleModalDelete" onclick="sendId(${item["colorID"]}, '${item["color_name"]}', '${item["product"].productID}')" data-toggle="modal" type="button">
                                                    <i class="fa-solid fa-trash" style="color: #212529"></i>
                                                    </button>                                                                         
                                                </td>
                                            </tr>`
                })

            }
            let lastChild = val.children[val.children.length - 1]
            document.querySelector(".activeTable") != null && document.querySelector(".activeTable").classList.remove("activeTable")
            lastChild.getAttribute("handleexit") == null ? lastChild.classList.add("activeTable") : lastChild.removeAttribute("handleExit")
        }


    }, 0)

}

function sendId(idColor, colorName, productName) {

    document.querySelector("#modal-body-delete").innerHTML = ` Lưu ý: khi xóa màu những ảnh có màu ${colorName} của sản phẩm ${productName}  cũng sẽ bị xóa. Bạn có chắc chắn muốn xóa`
    document.querySelector("#handleDeleteProductColor").setAttribute("href", `/admin/product/DeleteProductColor?ColorID=${idColor}`)

}

function handleEditProductColor(val) {
    document.querySelector("#handle5").classList.add("handle5")
    document.querySelector(".activeTable").setAttribute("style", "display:none")
    document.querySelector("#updateButtonProductColor").setAttribute("style", "display:block")
    document.querySelector("#updateButtonProductColor").setAttribute("class", "btn")
    document.querySelector("#colorIDPrd").setAttribute("value", val.getAttribute("class"))
    document.querySelector("#IDprd").setAttribute("value", val.parentElement.children[0].getAttribute("class"))
    document.querySelector("#ProductColorId").setAttribute("value", val.parentElement.children[0].innerHTML)
    document.querySelector("#colorname").setAttribute("value", val.parentElement.children[1].innerHTML)
    document.querySelector("#colorHex").setAttribute("value", val.parentElement.children[2].getAttribute("class"))
    document.querySelector("#available").setAttribute("value", val.parentElement.children[3].innerHTML)
}

document.querySelector("#exitHandle5").addEventListener("click", () => document.querySelector(".activeTable").removeAttribute("style")
)

function exitFormOrderDetail() {
    let dv = document.querySelector(".activeTable")
    dv.classList.remove("activeTable")
    dv.setAttribute("handleexit", "true")
}

window.addEventListener("load", () => {
    location.href.includes("/order") && document.querySelector(".oderIcon").setAttribute("style", "color:#fff; scale:1.1")
    location.href.includes("?type=category") && document.querySelector("#showFormFilter").setAttribute("style", "display:block")

})

function ProductColorTable() {
    let tr = document.querySelector("#sttProduct").children
    console.log("a")
    for (index = 0; index < tr.length; index++) {
        document.querySelector("#sttProduct").children[index].children[document.querySelector("#sttProduct").children[index].children.length - 1].setAttribute('style', 'top:420px')
    }
}

window.addEventListener("load", () => {
    let tr = document.querySelector("#sttProduct").children
    console.log("a")
    for (index = 0; index < tr.length; index++) {
        document.querySelector("#sttProduct").children[index].children[document.querySelector("#sttProduct").children[index].children.length - 1].classList.add("tableproductcolor")
    }
})

async function ListProduct(id) {
    const response = await fetch(`/admin/product/listProductColorByProductId/${id}`);

    const data = await response.json();
    localStorage.setItem("listProductColorByProductId", JSON.stringify(data))
}

document.querySelector("#btnEditProductImg", () => {
    let ProductIDList = document.querySelector("#ProductIDList").children
    for (idx = 0; idx < ProductIDList.length; idx++) {
        idx === 0 ? ProductIDList[0].setAttribute("selected", "selected") : ProductIDList[idx].removeAttribute("selected")
    }
})
function submitSearch(id){
    document.querySelector("#valtype").setAttribute("value", document.querySelector("#select-form").value)
    id.form.submit();
}