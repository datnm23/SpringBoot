const numbers = [11, 14, 16, 17, 23, 97];
const X = 30;
const result = findClosestPrime(numbers, X);
console.log(result)
console.log("_________________________________")

const result4 = findSecondLargest(numbers);
console.log(result4)

console.log("_________________________________")


function isPrime(num) {
  if (num <= 1) return false;
  for (let i = 2; i <= Math.sqrt(num); i++) {
    if (num % i === 0) return false;
  }
  return true;
}

function findClosestPrime(arr, X) {
  let closestPrime = null;
  let minDistance = 0;

  for (let num of arr) {
    if (isPrime(num)) {
      let distance = Math.abs(num - X);
      if (closestPrime === null || distance < minDistance) {
        minDistance = distance;
        closestPrime = num;
      }
    }
  }
  return closestPrime;
}

function reversibleNumber(num) {
  let newNum = 0;

  while (num > 0) {
    const lastNum = num % 10;
    newNum = (newNum * 10) + lastNum;
    num = parseInt(num / 10);
  }

  return newNum;
}

function isReversibleNumber(num) {
  return num === reversibleNumber(num);
}

function checkNumberContains(num) {
  while (num > 0) {
    const lastDigit = num % 10;
    if (lastDigit === 4) {
      return false;
    }
    num = parseInt(num / 10);
  }
  return true;
}

function findValidNumbers1() {
  for (let i = 10000; i <= 9999999; i++) {
    if (isPrime(i) && isReversibleNumber(i) && checkNumberContains(i)) {
      console.log(i)
    }
  }
}

function checkDigitsPrime(num) {
  while (num > 0) {
    const lastDigit = num % 10;
    if (lastDigit !== 2 && lastDigit !== 3 && lastDigit !== 5 && lastDigit !== 7) {
      return false;
    }
    num = parseInt(num / 10);
  }
  return true;
}

function findValidNumbers2() {
  for (let i = 1000000; i <= 9999999; i++) {
    if (isPrime(i) && checkDigitsPrime(i) && (isPrime(reversibleNumber(i)))) {
      console.log(i)
    }
  }
}

function findSecondLargest(arr) {
  if (arr.length < 2) {
      return "Array  need at least 2 member";
  }

  let max = null;
  let secondMax = null;
  let secondMaxIndex = -1;

  for (let i = 0; i < arr.length; i++) {
      const num = arr[i];
      if (max === null || num > max) {
          secondMax = max;
          max = num;
          secondMaxIndex = -1;
      } else if (num < max && (secondMax === null || num > secondMax)) {
          secondMax = num;
          secondMaxIndex = i;
      }
  }

  if (secondMax === null) {
      return ("There is no second largest number");
  }
  return {
    secondLargest: secondMax,
    index: secondMaxIndex
};
}