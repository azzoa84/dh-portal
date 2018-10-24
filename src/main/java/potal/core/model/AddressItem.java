package potal.core.model;

public class AddressItem {
	String roadAddr;	// 전체 도로명주소
	String jibunAddr;	// 지번주소
	String zipNo;		// 우편번호
	String detBdNmList;	// 상세건물명
	
	int totalCount;
	int currentPage;
	int countPerPage;
	
	
	public String getRoadAddr() {
		return roadAddr;
	}


	public void setRoadAddr(String roadAddr) {
		this.roadAddr = roadAddr;
	}


	public String getJibunAddr() {
		return jibunAddr;
	}


	public void setJibunAddr(String jibunAddr) {
		this.jibunAddr = jibunAddr;
	}


	public String getZipNo() {
		return zipNo;
	}


	public void setZipNo(String zipNo) {
		this.zipNo = zipNo;
	}


	public String getDetBdNmList() {
		return detBdNmList;
	}


	public void setDetBdNmList(String detBdNmList) {
		this.detBdNmList = detBdNmList;
	}


	public int getTotalCount() {
		return totalCount;
	}


	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}


	public int getCurrentPage() {
		return currentPage;
	}


	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}


	public int getCountPerPage() {
		return countPerPage;
	}


	public void setCountPerPage(int countPerPage) {
		this.countPerPage = countPerPage;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[roadAddr=" + roadAddr + ", jibunAddr=" + jibunAddr + ", zipNo=" + zipNo + ", detBdNmList=" + detBdNmList + 
				", totalCount=" + totalCount + ", currentPage=" + currentPage + ", countPerPage=" + countPerPage + "]";
	}
}