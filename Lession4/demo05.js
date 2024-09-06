const btn = document.getElementById('btn');
const select = document.getElementById('breed-list');
const result = document.getElementById('result');

async function getBreedList() {
  let res = await axios.get("https://dog.ceo/api/breeds/list/all")
  renderBreed(res.data.message)
}

function renderBreed(breeds) {
  for (element in breeds) {
    const option = document.createElement("option")
    option.innerHTML = element
    select.appendChild(option)

  }
}

async function renderSubBreed() {
  const breed = select.value;
  console.log(breed)
  const url = `https://dog.ceo/api/breed/${breed}/list`;
  let res = await axios.get(url);
  const subBreeds = res.data.message;

  if (subBreeds.length < 1) {
    result.innerText = "Không có sub list";
  } else {
    const ul = document.createElement('ul');
    for (let i = 0; i < subBreeds.length; i++) {
      const li = document.createElement('li');
      li.innerText = subBreeds[i];
      ul.appendChild(li);
    }
    result.appendChild(ul);
  }
}
getBreedList();