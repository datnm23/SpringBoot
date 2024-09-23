// Bài 1

function getStringsWithMaxLength(arr) {
  let maxLength = 0;
  for (let str of arr) {
    if (str.length > maxLength) {
      maxLength = str.length;
    }
  }
  
  const result = [];
  for (let str of arr) {
    if (str.length === maxLength) {
      result.push(str);
    }
  }
  
  return result;
}
const result = getStringsWithMaxLength(['aba', 'aa', 'ad', 'c', 'vcd']);
console.log(result);

const users = [
  {
      name: "Bùi Công Sơn",
      age: 30,
      isStatus: true
  },
  {
      name: "Nguyễn Thu Hằng",
      age: 27,
      isStatus: false
  },
  {
      name: "Phạm Văn Dũng",
      age: 20,
      isStatus: false
  }
];

// Bài 2
// 2.1

function getActiveUsersOver25(usersArray) {
  return usersArray.filter(function(user) {
    return user.age > 25 && user.isStatus === true;
  });
}

const activeUsersOver25 = getActiveUsersOver25(users);
console.log("Các user có age > 25 và isStatus = true:");
console.log(activeUsersOver25);

// 2.2

function getUsersSortedByAge(usersArray) {
  return usersArray.slice().sort(function(a, b) {
    return a.age - b.age;
  });
}

const usersSortedByAgeDesc = sortUsersByAgeDescending(users);
console.log("Các user được sắp xếp theo age giảm dần:");
console.log(usersSortedByAgeDesc);

// Bài 3