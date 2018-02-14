/*
 * jQuery Client Side Excel Export Plugin Library
 * http://techbytarun.com/
 *
 * Copyright (c) 2013 Batta Tech Private Limited
 * https://github.com/tarunbatta/ExcelExportJs/blob/master/LICENSE.txt
 */

(function ($) {
    var $defaults = {
        containerid: null
        , datatype: 'table'
        , dataset: null
        , columns: null
        , returnUri: false
        , cssStyle: ''
        , worksheetName: "Sheet1"
        , encoding: "utf-8"
    };

    var $settings = $defaults;

    this.excelexportjs = function (options) {
        $settings = jQuery.extend({}, $defaults, options);

        var gridData = [];
        var excelData;

        return Initialize();

        function Initialize() {
            var type = $settings.datatype.toLowerCase();

            BuildDataStructure(type);

            switch (type) {
                case 'table':
                    excelData = Export(ConvertFromTable());
                    break;
                case 'json':
                    excelData = Export(ConvertDataStructureToTable());
                    break;
                case 'xml':
                    excelData = Export(ConvertDataStructureToTable());
                    break;
                case 'jqgrid':
                    excelData = Export(ConvertDataStructureToTable());
                    break;
            }
            if ($settings.returnUri) {
                return excelData;
            }
            else {
            	// window.open(excelData);
            	// 위 구문대신 아래 구문으로 변경
            	
            	if (document.getElementById('techbytarun_excel_export') == null) {
            		var d_html = "<a id='techbytarun_excel_export' style='display:none;' /> ";
            		document.body.insertAdjacentHTML('beforeEnd', d_html);
            	}
            	var dlnk = document.getElementById('techbytarun_excel_export');
            	dlnk.href = excelData;
            	dlnk.download = options.xlsFileName + ".xls";
            	
            	dlnk.click();
            }
        }

        function BuildDataStructure(type) {
            switch (type) {
                case 'table':
                    break;
                case 'json':
                    gridData = $settings.dataset;
                    break;
                case 'xml':
                    jQuery($settings.dataset).find("row").each(function (key, value) {
                        var item = {};

                        if (this.attributes != null && this.attributes.length > 0) {
                            jQuery(this.attributes).each(function () {
                                item[this.name] = this.value;
                            });

                            gridData.push(item);
                        }
                    });
                    break;
                case 'jqgrid':
                    jQuery($settings.dataset).find("rows > row").each(function (key, value) {
                        var item = {};

                        if (this.children != null && this.children.length > 0) {
                            jQuery(this.children).each(function () {
                                item[this.tagName] = jQuery(this).text();
                            });

                            gridData.push(item);
                        }
                    });
                    break;
            }
        }

        function ConvertFromTable() {
            var result = jQuery('<div>').append(jQuery('#' + $settings.containerid).clone()).html();
            return result;
        }

        function ConvertDataStructureToTable() {
            var result = "<table>";

            result += "<thead><tr>";
            jQuery($settings.columns).each(function (key, value) {
                if (this.ishidden != true && this.headerHidden != true) {
                	if (this.colspan != 0 && this.colspan != 99 && !this.rowspan) {
                		result += "<th colspan = '" + this.colspan + "' ";
                    }
                	else if (this.colspan == 0) 
                		result += "<th";
                	else if (this.rowspan != 0 && this.rowspan != 99) 
                		result += "<th rowspan = '" + this.rowspan + "' ";
                	else if (this.rowspan == 0 && this.rowspan)
                		result += "<th";
                	else if (this.colspan == 99)
                		return true;
                	
                    if (this.width != null) {
                        result += " style='width: " + this.width + "'";
                    }
                    result += ">";
                    result += this.headertext;
                    result += "</th>";
                }
            });
            result += "</tr>";
            
            if ($settings.columns[0].headRowCount > 1) {
	            result += "<tr>";
	            jQuery($settings.columns).each(function (key, value) {
	                if (this.ishidden != true) {
	                	if (this.rowspan != 0 && this.rowspan != 99) 
	                		return true;
	                	else
	                		result += "<th";
	                	
	                	if (this.width != null) {
	                        result += " style='width: " + this.width + "'";
	                    }
	                    result += ">";
	                    result += this.headertext2;
	                    result += "</th>";
	                }
	            });
	            result += "</tr></thead>";
            }
            else {
            	result += "</thead>";
            }
            
            result += "<tbody>";
            jQuery(gridData).each(function (key, value) {
                result += "<tr>";
                jQuery($settings.columns).each(function (k, v) {
                    if (value.hasOwnProperty(this.datafield)) {
                        if (this.ishidden != true) {
                            result += "<td";
                            if (this.width != null) {
                                result += " style='width: " + this.width + "'";
                            }
                            result += ">";
                            result += value[this.datafield];
                            result += "</td>";
                        }
                    }
                });
                result += "</tr>";
            });
            result += "</tbody>";

            result += "</table>";
            return result;
        }

        function Export(htmltable) {
        	var excelFile = "<html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:x='urn:schemas-microsoft-com:office:excel' xmlns='http://www.w3.org/TR/REC-html40'>";
            excelFile += "<head>";
            excelFile += '<meta http-equiv="Content-type" content="text/html;charset=' + $defaults.encoding + '" />';
            excelFile += "<!--[if gte mso 9]>";
            excelFile += "<xml>";
            excelFile += "<x:ExcelWorkbook>";
            excelFile += "<x:ExcelWorksheets>";
            excelFile += "<x:ExcelWorksheet>";
            excelFile += "<x:Name>";
            excelFile += "{worksheet}";
            excelFile += "</x:Name>";
            excelFile += "<x:WorksheetOptions>";
            excelFile += "<x:DisplayGridlines/>";
            excelFile += "</x:WorksheetOptions>";
            excelFile += "</x:ExcelWorksheet>";
            excelFile += "</x:ExcelWorksheets>";
            excelFile += "</x:ExcelWorkbook>";
            excelFile += "</xml>";
            excelFile += "<![endif]-->";
            excelFile += "<style type='text/css'>";
            excelFile += $settings.cssStyle;
            excelFile += "</style>";
            excelFile += "</head>";
            excelFile += "<body>";
            excelFile += htmltable.replace(/"/g, '\'');
            excelFile += "</body>";
            excelFile += "</html>";
            
            //var uri = "data:application/vnd.ms-excel;base64,";
            // (170614) 구글 확장프로그램(오피스 웹오픈) 연결 안되도록 처리
            var uri = "data:application/octet-stream;base64,";
            var ctx = { worksheet: $settings.worksheetName, table: htmltable };
            return (uri + base64(format(excelFile, ctx)));
        }

        function base64(s) {
            return window.btoa(unescape(encodeURIComponent(s)));
        }

        function format(s, c) {
            return s.replace(/{(\w+)}/g, function (m, p) { return c[p]; });
        }
    };
})(window.jQuery);
