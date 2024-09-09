let colors = [
  '#3498db',
  '#9b59b6',
  '#e74c3c',
  '#2c3e50',
  '#d35400',
]

const btn = document.getElementById('btn');
const boxes = document.getElementsByClassName('boxes')[0];
let points = document.getElementsByClassName('points')[0];
let boxCount = 5;

function createBoxes() {
  for (let i = 0; i < colors.length; i++) {
      const box = document.createElement('div');
      box.className = 'box';
      box.style.backgroundColor = colors[i % colors.length];
      box.addEventListener("click", function(){
        box.remove;
        boxCount--;
      })
      boxes.appendChild(box);
  }
}


btn.addEventListener('click', function() {
  createBoxes();
  boxCount+=5;
});

points.textContent = boxCount;
createBoxes();