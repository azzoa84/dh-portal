var serviceProvider = (function () {
	/** https://docs.angularjs.org/api **/
    /*
    @ 서비스 적용 (화면별 js 컨트롤러에서 적용)
    $scope.messages = $injector.get('messages');
    */
    return [
		/** 클라이언트 사용 메시지 정의 */
		['messages',
			{
				'MSG0001':'핑거세일즈에 오신것을 환영합니다.',
				'MSG0002':'안녕히가세요.'
			}
		]
		
    ];
}());

