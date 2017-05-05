function getVirtulData(year) {
	year = year || '2017';
	var date = +echarts.number.parseDate(year + '-05-11');
	var end = +echarts.number.parseDate((year) + '-11-11');
	var dayTime = 3600 * 24 * 1000;
	var data = [];
	$.ajax({//获取日数据
		type:'GET',
		url:'day.do',//////////////////////
		dataType:'json',
		data:'day',
		success:function(d){//{"day":[12,10,......]}
			for (var time = date,i=0; time < end; time += dayTime,i++) {
				data.push([
					echarts.format.formatTime('yyyy-MM-dd', time),
					d.day[i]
				]);
			}
		}
	});
	for (var time = date; time < end; time += dayTime) {
		data.push([
			echarts.format.formatTime('yyyy-MM-dd', time),
			Math.floor(Math.random() * 1000)
		]);
	}
	return data;
}
function init(){
	var body=document.getElementsByTagName('body')[0];
	$("body").css("width",window.screen.width+"px");
	$("body").css("height",window.screen.height*0.83+"px");
	$("#main").css("height",parseFloat($("body").css("height"))*0.65);
	$("#mFilter").css("height",parseFloat($("body").css("height"))*0.65);
	$("#dFilter").css("height",parseFloat($("body").css("height"))*0.35);

	var mainChart=echarts.init(document.getElementById('main'));
	var monChart=echarts.init(document.getElementById('mFilter'));
	var dayChart=echarts.init(document.getElementById('dFilter'));

	//主视图的配置
	 var mainOption = {
			 tooltip: {
			        trigger: 'item',
			        formatter: "{a} <br/>{b}: {c} ({d}%)"
			    },
			    legend: {
			        orient: 'vertical',
			        x: 'left',
			        data:['女','男','小于18','18-24','25-29','30-34','35-39','40-49','大于50']
			    },
			    series: [
			        {
			            name:'gender',
			            type:'pie',
			            selectedMode: 'single',
			            radius: [0, '30%'],

			            label: {
			                normal: {
			                    position: 'inner'
			                }
			            },
			            labelLine: {
			                normal: {
			                    show: false
			                }
			            },
			            data:[]
			        },
			        {
			            name:'age',
			            type:'pie',
			            radius: ['40%', '55%'],

			            data:[]
			        }
			    ]
	        };
	 //月选择器配置
	 var monOption = {
			    title: {
			        text: 'Customized Pie',
			        left: 'center',
			        top: 20,
			        textStyle: {
			            color: '#ccc'
			        }
			    },

			    tooltip : {
			        trigger: 'item',
			        formatter: "{a} <br/>{b} : {c} ({d}%)"
			    },

			    visualMap: {
			        show: false,
			        min: 80,
			        max: 600,
			        inRange: {
			            colorLightness: [0, 1]
			        }
			    },
			    series : [
			        {
			            name:'month',
			            type:'pie',
			            radius : '55%',
			            center: ['50%', '50%'],
			            data:[],
			            roseType: 'angle',
			            label: {
			                normal: {
			                    textStyle: {
			                        color: 'rgba(255, 255, 255, 0.3)'
			                    }
			                }
			            },
			            labelLine: {
			                normal: {
			                    lineStyle: {
			                        color: 'rgba(255, 255, 255, 0.3)'
			                    },
			                    smooth: 0.2,
			                    length: 10,
			                    length2: 20
			                }
			            },
			            itemStyle: {
			                normal: {
			                    color: '#c23531',
			                    shadowBlur: 200,
			                    shadowColor: 'rgba(0, 0, 0, 0.5)'
			                }
			            },

			            animationType: 'scale',
			            animationEasing: 'elasticOut',
			            animationDelay: function (idx) {
			                return Math.random() * 200;
			            }
			        }
			    ]
			};
	var dOption={//天选择器配置
		tooltip: {
			position: 'top'
		},
		visualMap: {
			min: 0,
			max: 1000,
			calculable: true,
			orient: 'horizontal',
			left: 'center',
			top: 'top'
		},
		calendar: [
			{
				top: 100,
				range: '2015',
				cellSize: ['auto', 20],
				right: 5
			}],
		series: [{
			type: 'heatmap',
			coordinateSystem: 'calendar',
			calendarIndex: 0,
			data: getVirtulData(2015)
		}]
	};
	dayChart.setOption(dOption);
	$.ajax({//获取主视图数据
		type:'GET',
		url:'main.do',//////////////////////
		dataType:'json',
		data:'all',
		success:function(d){//{"age1":xx,"age2":xx,.....}
			mainChart.setOption({
				series:[
					{
						name:'age',
						data:[
							{value:d.age1, name:'小于18'},
							{value:d.age2, name:'18-24'},
							{value:d.age3, name:'25-29'},
							{value:d.age4, name:'30-34'},
							{value:d.age5, name:'35-39'},
							{value:d.age6, name:'40-49'},
							{value:d.age7+ d.age8, name:'大于50'}
						]
					},
					{
						name:'gender',
						data:[
							{value: d.male, name:'男', selected:true},
							{value: d.female, name:'女'}
						]
					}
				]
			});
		}
	});
	$.ajax({//获取月份数据
		type:'GET',
		url:'month.do',//////////////////////
		dataType:'json',
		data:'month',
		success:function(d){//{"May":xx,"Jun":xx,......}
			monChart.setOption({
				series:[{
					name:'month',
					data:[
						{value: d.May, name:'May'},
						{value: d.Jun, name:'Jun'},
						{value: d.Jly, name:'Jly'},
						{value: d.Aug, name:'Aug'},
						{value: d.Sep, name:'Sep'},
						{value: d.Oct, name:'Oct'},
						{value: d.Nov, name:'Nov'}
					].sort(function (a, b) { return a.value - b.value})
				}]
			});
		}
	});
	dayChart.on("click",function(par){
		$.ajax({
			type:'GET',
			url:'main.do',/////////////////////////////////////////////////////////////
			dataType:'json',
			data:par[0],//yyyy-mm-dd
			success:function(d){
				//{"age1":xx,"age2":xx,.....}
				mainChart.setOption({
					series:[
						{
							name:'age',
							data:[
								{value:d.age1, name:'小于18'},
								{value:d.age2, name:'18-24'},
								{value:d.age3, name:'25-29'},
								{value:d.age4, name:'30-34'},
								{value:d.age5, name:'35-39'},
								{value:d.age6, name:'40-49'},
								{value:d.age7+ d.age8, name:'大于50'}
							]
						},
						{
							name:'gender',
							data:[
								{value: d.male, name:'男', selected:true},
								{value: d.female, name:'女'}
							]
						}
					]
				});
			}
		});
	});
	monChart.on("click",function(par){
		$.ajax({
			type:'GET',
			url:'main.do',/////////////////////////////////////////////////////////////
			dataType:'json',
			data:par.data.name,//String eg:'May'
			success:function(d){//返回统计值（数字）
				//{"age1":xx,"age2":xx,.....}
				mainChart.setOption({
					series:[
						{
							name:'age',
							data:[
								{value:d.age1, name:'小于18'},
								{value:d.age2, name:'18-24'},
								{value:d.age3, name:'25-29'},
								{value:d.age4, name:'30-34'},
								{value:d.age5, name:'35-39'},
								{value:d.age6, name:'40-49'},
								{value:d.age7+ d.age8, name:'大于50'}
							]
						},
						{
							name:'gender',
							data:[
								{value: d.male, name:'男', selected:true},
								{value: d.female, name:'女'}
							]
						}
					]
				});
			}
		});
	});
}