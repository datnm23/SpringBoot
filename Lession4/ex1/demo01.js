const result = document.getElementById('result');
const searchBox = document.getElementById('search-box');

async function getMealList() {
  const searchword = searchBox.value.trim();
  if (!searchword) {
    result.innerText = 'Please enter a search word.';
    return;
  }

  let res = await axios.get(`https://www.themealdb.com/api/json/v1/1/search.php?s=${searchword}`);
  const mealList = res.data.meals;
  if (mealList) {
    for (let i = 0; i < mealList.length; i++) {
      const meal = mealList[i];
      console.log(meal)
      const mealDiv = document.createElement("div")
      mealDiv.className = 'meal';
      mealDiv.innerHTML = `
      <h3>${meal.strMeal}</h3>
      <img src="${meal.strMealThumb}" alt="${meal.strMeal}" style="width: 100px; height: 100px;">
      <p>${meal.strInstructions.substring(0, 100)}...</p>
    `;
        result.appendChild(mealDiv);
    }
  }else {
    result.innerText = "No meals found.";
  }

}
getMealList();