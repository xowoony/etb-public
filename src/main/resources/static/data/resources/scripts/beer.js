
// 맥주 카테고리별 보기
const url = new URL(window.location.href);
const searchParams = url.searchParams
const beerIndex = searchParams.get('beerIndex');

const beerCategory = window.document.getElementById('beerCategory');
beerCategory.onchange = e => {
    e.preventDefault();
    const value = (beerCategory.options[beerCategory.selectedIndex].value);
    window.location.href = `./beer?beerIndex=${beerIndex}&beerCategory=${value}`;
}