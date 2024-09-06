// tu khoa async trong dau funcition cho biet trong function co nhung hanh dong chowf doiwj nhau
// ham 


// Gọi API để lấy dữ liệu và hiển thị
async function getRandomImage() {
    try {
        // Gọi API lấy ảnh random của dog
        let res = await axios.get("https://dog.ceo/api/breeds/image/random")

        // Gán URL cho thẻ image
        document.getElementById("image").setAttribute("src",res.data.message)
    } catch (error) {
        console.log(error);
    }
}

