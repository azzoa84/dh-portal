/**
 * [jQuery Full Calendar 초기세팅]
 */
function getCalendarConfig() {
	return {
        header : {
            left : '',
            center : 'title',
            right : 'month,basicWeek'
            /* basicWeek, basicDay' */
        },
        titleFormat : {
            month : 'yyyy년 MMMM',
            week : "yyyy년 MMMM d[ yyyy]{'일 ~'[ MMM] dd일 }",
            day : 'yyyy년 MMM d dddd'
        },
        columnFormat : {
            month : 'ddd',
            week : 'M/d ddd ',
            day : 'M월d일 dddd '
        },
        timeFormat : { // for event elements
            '' : 'HH:mm', // 월간
            agenda : 'HH:mm{ - HH:mm}' // 주간,일간
        },
        allDayText : '시간', // 주간,월간
        axisFormat : 'tt hh', // 주간,월간

        monthNames : [ '1월', '2월', '3월',
                '4월', '5월', '6월', '7월',
                '8월', '9월', '10월', '11월',
                '12월' ],
        monthNamesShort : [ '1월', '2월',
                '3월', '4월', '5월', '6월',
                '7월', '8월', '9월', '10월',
                '11월', '12월' ],
        dayNames : [ '일요일', '월요일', '화요일',
                '수요일', '목요일', '금요일', '토요일' ],
        dayNamesShort : [ '일', '월', '화',
                '수', '목', '금', '토' ],
        buttonText : {
            //prev : '&nbsp;&#9668;&nbsp;',
            //next : '&nbsp;&#9658;&nbsp;',
            prevYear : '&nbsp;&lt;&lt;&nbsp;',
            nextYear : '&nbsp;&gt;&gt;&nbsp;',
            today : '오늘',
            month : '월간',
            week: '주간',
            day: '일간'
        },
        firstDay : 1,
        editable : false
	};
};

function displayPrevAndNextBtn()
{
	var prev = $(".fc-header-center").prepend('<span class="fc-text-arrow" style="font-weight: bold; margin-right: 0.6em; cursor:pointer;">&lt;</span>').find('span:first');
	prev.click(function() { $('#calendar').fullCalendar('prev'); });
	
	var next = $(".fc-header-center").append('<span class="fc-text-arrow" style="font-weight: bold; margin-left: 0.6em; cursor:pointer;">&gt;</span>').find('span:last');
	next.click(function() { $('#calendar').fullCalendar('next'); });
	
};