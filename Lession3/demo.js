// const element= document.getElementById("heading");
// element.style.color="red";
// element.textContent = element.textContent.toUpperCase();

// const classlist = document.getElementsByClassName("para");

// for (let i = 0; i < classlist.length; i++) {
//   classlist[i].style.color = "blue";
//   classlist[i].style.fontSize = "20px";
// }

// let link = document.createElement('a');
// link.href = 'https://www.facebook.com';
// link.innerHTML = 'Facebook';

// document.getElementsByClassName('para-3')[0].insertAdjacentElement('afterend', link);

// let titleElement = document.getElementById('title');
// titleElement.textContent = 'Đây là thẻ tiêu đề';

// let descriptionElement = document.querySelector('.description');
// descriptionElement.classList.add('text-bold');

// let button = document.createElement('button');
// button.innerHTML = 'Click me';

// document.getElementsByClassName('para-3')[0].replaceWith(button);


const paragraph = document.getElementsByTagName("p");

const words = paragraph[0].innerText.split(' ');
paragraph[0].innerText = '';
for (let i = 0; i < words.length; i++) {
  const wordSpan = document.createElement('span');
  wordSpan.innerText = words[i];

  if (words[i].length >= 5) {
    wordSpan.style.color = 'yellow';
  }

  paragraph[0].appendChild(wordSpan);

  if (i < words.length - 1) {
    paragraph[0].appendChild(document.createTextNode(' '));
  }
}
  //2
  const paragraph1 = document.getElementsByTagName("p")[0];
  const link = document.createElement("a");
  link.href = "https://www.facebook.com";
  link.innerText = "facebook";
  paragraph1.insertAdjacentElement("afterend", link);
  
  // 3
  const paragraph3 = document.getElementsByTagName("p")[0];
  const words3 = paragraph3.innerText.split(' ');
  const wordCount = words3.length;
  const divCount = document.createElement("div");

  divCount.innerText = `Số từ trong đoạn văn: ${wordCount}`;
  document.body.appendChild(divCount);

  //4
  const paragraph4 = document.getElementsByTagName("p")[0];
  let text = paragraph4.innerText;

  text = text.replace(/,/g, '🤔').replace(/\./g, '😲');
  paragraph4.innerText = text