let slideContainer = document.querySelector('.slide_img');
let slides = document.querySelectorAll('.slide');
let prevbtn = document.querySelector('.prev');
let nextbtn = document.querySelector('.next');


$(function(){
    var container = $(".slide_wrap");
    var slideShow = container.find(".slide_show");
    var slideImg = slideShow.find(".slide_img");
    var slides = slideImg.find(">div");                //슬라이드 이미지
    var slideBtn = container.find(".slide_btn")        //슬라이드 버튼

    var slideCount = slides.length;                    //슬라이드 개수
    var slideWidth = slides.innerWidth();                   //슬라이드 이미지의 가로 값
    var show_num = 3;
    var num = 0;

    var slideCopy = $(".slide:lt("+show_num+")").clone();
    slideImg.append(slideCopy);

    //이미지 움직이기
    function back(){
        if(num == 0){
            num == slideCount;
            slideImg.css("margin-left",-num * slideWidth +"px" )
        }
        num--;
        slideImg.animate({"margin-left": -slideWidth * num+"px"}, 500);
    };
    function next(){
        if(num == slideCount){
            num == 0;
            slideImg.css("margin-left",-num * slideWidth +"px");
        }
        num++;
        slideImg.animate({"margin-left": -slideWidth * num+"px"}, 500);
    };

    //버튼 클릭하기
    slideBtn.on("click", "a", function(){
        if($(this).hasClass("prev")){
            back();
        } else {
            next();
        }
    });

});