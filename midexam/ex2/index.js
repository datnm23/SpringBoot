const quizes = [
  {
      id: 1,
      question: "1 + 1 = ?",
      answers: [1, 2, 3, 4],
  },
  {
      id: 2,
      question: "2 + 2 = ?",
      answers: [3, 4, 5, 6],
  },
  {
      id: 3,
      question: "3 + 3 = ?",
      answers: [5, 6, 7, 8],
  },
];

function renderQuiz() {
  const quizContainer = document.getElementsByClassName('quiz-container')[0];
  quizContainer.innerHTML = '';

  for (let i = 0; i < quizes.length; i++) {
      const quiz = quizes[i];
      const quizItem = document.createElement('div');
      quizItem.className = 'quiz-item';

      const questionTitle = document.createElement('h3');
      questionTitle.textContent = 'Câu ' + quiz.id + ' : ' + quiz.question;
      quizItem.appendChild(questionTitle);

      const quizAnswer = document.createElement('div');
      quizAnswer.className = 'quiz-answer';

      for (let j = 0; j < quiz.answers.length; j++) {
          const answer = quiz.answers[j];

          const quizAnswerItem = document.createElement('div');
          quizAnswerItem.className = 'quiz-answer-item';

          const input = document.createElement('input');
          input.type = 'radio';
          input.name = 'quiz' + quiz.id; 
          input.id = 'q' + quiz.id + '_a' + j;
          input.value = answer;

          const label = document.createElement('label');
          label.htmlFor = input.id;
          label.textContent = answer;

          quizAnswerItem.appendChild(input);
          quizAnswerItem.appendChild(label);
          quizAnswer.appendChild(quizAnswerItem);
      }

      quizItem.appendChild(quizAnswer);
      quizContainer.appendChild(quizItem);
  }
}

function randomAnswers() {
  for (let i = 0; i < quizes.length; i++) {
      const quiz = quizes[i];
      const answerInputs = document.getElementsByName('quiz' + quiz.id);
      const randomIndex = Math.floor(Math.random() * answerInputs.length);
      for (let j = 0; j < answerInputs.length; j++) {
          answerInputs[j].checked = false;
      }
      answerInputs[randomIndex].checked = true;
  }
}

window.onload = function() {
  renderQuiz();
  const btn = document.getElementById('btn');
  btn.onclick = randomAnswers;
};