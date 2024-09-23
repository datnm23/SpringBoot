function fetchData(typeList) {
  const url = 'https://jsonplaceholder.typicode.com/' + typeList + '?_limit=50';

  const req = new XMLHttpRequest();
  req.open('GET', url);
  req.onload = function() {
      const data = JSON.parse(req.responseText);
      displayData(data); 
  };
  req.send();
}

function displayData(data) {
  const dataList = document.getElementById('data-list');
  dataList.innerHTML = '';

  for (let i = 0; i < data.length; i++) {
      const item = data[i];
      const li = document.createElement('li');
      li.textContent = item.title;
      dataList.appendChild(li);
  }
}

function setActiveButton(buttonId) {
  const buttons = document.querySelectorAll('button');
  buttons.forEach(function(button) {
      button.classList.remove('active');
  });

  const activeButton = document.getElementById(buttonId);
  activeButton.classList.add('active');
}

document.getElementById('btn-posts').onclick = function() {
  fetchData('posts');
  setActiveButton('btn-posts');
};

document.getElementById('btn-albums').onclick = function() {
  fetchData('albums');
  setActiveButton('btn-albums');
};

document.getElementById('btn-photos').onclick = function() {
  fetchData('photos');
  setActiveButton('btn-photos');
};

fetchData('posts');
setActiveButton('btn-posts');