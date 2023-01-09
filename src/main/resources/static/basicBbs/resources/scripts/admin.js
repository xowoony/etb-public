// 전체 체크박스
const checkboxes
    = document.querySelectorAll('input[name="reportedArticle"]');

function checkSelectAll()  {
    // 선택된 체크박스
    const checked
        = document.querySelectorAll('input[name="reportedArticle"]:checked');
    // select all 체크박스
    const selectAll
        = document.querySelector('input[name="selectAll"]');

    if(checkboxes.length === checked.length)  {
        selectAll.checked = true;
    }else {
        selectAll.checked = false;
    }

}

function selectAllArticles(selectAll)  {
    checkboxes.forEach((checkbox) => {
        checkbox.checked = selectAll.checked
    });
}