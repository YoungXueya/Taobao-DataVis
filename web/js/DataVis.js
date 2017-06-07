function init(){
	var body=document.getElementsByTagName('body')[0];
	$("body").css("width",window.screen.width*0.9+"px");
	$("body").css("height",window.screen.height*0.75+"px");
	$("#main").css("height",parseFloat($("body").css("height"))*0.65+"px");
	$("#mFilter").css("height",parseFloat($("body").css("height"))*0.65+"px");
	$("#dFilter").css("height",parseFloat($("body").css("height"))*0.35+"px");
    $("#dFilter").css("width",parseFloat($("body").css("width")));
    $("#mFilter").css("width",parseFloat($("body").css("width"))*0.5+"px");
    $("#main").css("width",parseFloat($("body").css("width"))*0.5+"px");

	var mainChart=echarts.init(document.getElementById('main'));
	var monChart=echarts.init(document.getElementById('mFilter'));
	var dayChart=echarts.init(document.getElementById('dFilter'));

	//主视图的配置
	 var mainOption = {
		 backgroundColor: '#eee',
			 	tooltip: {
			        trigger: 'item',
			        formatter: "{a} <br/>{b}: {c} ({d}%)"
			    },
		 		legend: {
			        orient: 'vertical',
			        x: 'left',
					left:'5%',
					top:'5%',
			        data:[]
			    },
			    series: [
			        {
			            name:'gender',
			            type:'pie',
			            selectedMode: 'single',
			            radius: [0, '40%'],

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
			            data:[],
						color:['#B4D2ED','#FFCCCC','#aaa']
			        },
			        {
			            name:'age',
			            type:'pie',
			            radius: ['60%', '85%'],
			            data:[],
						color:['#F2E1AC','#F2B28C','#F2BB9B','#F29E95','#CD9DA7','#B4D2ED','#ABC4A6']
			        }
			    ],
		 //color:['#FFCCCC','#F2D9D9','#E6E6E6','#D9F2F2','#CCFFFF','#FF9966','#F2B373','#E6CC80','#D9E68C','#CCFF99']
	 };
	mainChart.setOption(mainOption);
	$.ajax({//获取主视图数据
		type:'GET',
		url:'age_gender.do',//////////////////////
		dataType:'json',
		success:function(d){//{"age1":xx,"age2":xx,.....}
			mainChart.setOption({
				legend: {
			        data:['女','男','小于18','18到24','25到29','30到34','35到39','40到49','大于50','未知']
			    },
				series:[
					{
						name:'age',
			            type:'pie',
			            radius: ['60%', '85%'],
						data:[
							{value:d.Age1, name:'小于18'},
							{value:d.Age2, name:'18到24'},
							{value:d.Age3, name:'25到29'},
							{value:d.Age4, name:'30到34'},
							{value:d.Age5, name:'35到39'},
							{value:d.Age6, name:'40到49'},
							{value:d.Age7, name:'大于50'}
						]
					},
					{
						name:'gender',
			            type:'pie',
			            radius: [0, '40%'],
						data:[
							{value: d.male, name:'男'},
							{value: d.female, name:'女'},
							{value: d.gunknown, name:'未知'}
						]
					}
				]
			});
		}
	});
	 //月选择器配置
	 var monOption = {
		 backgroundColor: '#eee',
			    title: {
			        text: '月份选择器',
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

			    // visualMap: {
			    //     show: false,
			    //     min: 20,
			    //     max: 95,
			    //     inRange: {
			    //         colorLightness: [0, 1]
			    //     }
			    // },
			    series : [
			        {
			            name:'month',
			            type:'pie',
			            radius : '75%',
			            center: ['50%', '50%'],
			            data:[],
			            roseType: 'angle',
			            label: {
			                normal: {
			                    textStyle: {
			                        color: '#555'
			                    }
			                }
			            },
			            labelLine: {
			                normal: {
			                    lineStyle: {
			                        color: '#555'
			                    },
			                    smooth: 0.2,
			                    length: 10,
			                    length2: 20
			                }
			            },
						// itemStyle: {
						// 		normal: {
						// 			color: '#FF6600',
						// 			shadowBlur: 200,
						// 			shadowColor: 'rgba(0, 0, 0, 0.5)'
						// 		}
						// 	},
			            animationType: 'scale',
			            animationEasing: 'elasticOut',
			            animationDelay: function (idx) {
			                return Math.random() * 200;
			            },
		 			color:['#FF6600','#FF8011','#FF9922','#FFB333','#FFCC44','#FFE655','#FFFF66']
			        }
			    ]
			};
	monChart.setOption(monOption);
	var dOption={//天选择器配置
		backgroundColor: '#eee',
		tooltip: {
			position: 'top'
		},
		visualMap: {
			min: 0,
			max: 30,
			calculable: true,
			orient: 'horizontal',
			left: 'center',
			top: 'top'
		},
		calendar: [
			{
				//top: 50,
				range: ['2015-05-11', '2015-11-12'],
				cellSize: ["auto",20],
				right: 10
			}],
		series: [{
			type: 'heatmap',
			coordinateSystem: 'calendar',
			calendarIndex: 0,
			data:[]
		}]
	};
	dayChart.setOption(dOption);

	$.ajax({//获取日数据
		type:'GET',
		url:'day.do',//////////////////////
		dataType:'json',
		success:function(d){//{"day":[12,10,......]}
			var date = +echarts.number.parseDate('2015' + '-05-11');
			var end = +echarts.number.parseDate(('2015') + '-11-12');
			var dayTime = 3600 * 24 * 1000;
			var data = [];
			for (var time = date,i=0; time < end; time += dayTime,i++) {
				data.push([
					echarts.format.formatTime('yyyy-MM-dd', time),
					d.day[i]
				]);
			}
			dayChart.setOption({
				series:[{
					type: 'heatmap',
					coordinateSystem: 'calendar',
					calendarIndex: 0,
					data:data
				}]
			});
		}
	});

	$.ajax({//获取月份数据
		type:'GET',
		url:'month.do',//////////////////////
		dataType:'json',
		success:function(d){//{"May":xx,"Jun":xx,......}
			console.log(d);
			monChart.setOption({
				series:[{
					type:'pie',
					name:'month',
					data:[
						{value: d.May, name:'May'},
						{value: d.Jun, name:'Jun'},
						{value: d.Jly, name:'Jly'},
						{value: d.Aug, name:'Aug'},
						{value: d.Sep, name:'Sep'},
						{value: d.Oct, name:'Oct'},
						{value: d.Noc, name:'Nov'}
					]
				}],

		 		color:['#FFE957','#F5ED79','#EBF29A','#E0F6BC','#D6FBDD','#CCFFFF']
			});
		}
	});
	dayChart.on("click",function(par){
		$.ajax({
			type:'GET',
			url:'certainDay.do?day='+par.data[0],//yyyy-mm-dd
			dataType:'json',
			success:function(d){
				//{"age1":xx,"age2":xx,.....}
				mainChart.setOption({
					legend: {
			        data:['女','男','小于18','18到24','25到29','30到34','35到39','40到49','大于50','未知']
			    },
				series:[
					{
						name:'age',
			            type:'pie',
			            radius: ['60%', '85%'],
						data:[
							{value:d.Age1, name:'小于18'},
							{value:d.Age2, name:'18到24'},
							{value:d.Age3, name:'25到29'},
							{value:d.Age4, name:'30到34'},
							{value:d.Age5, name:'35到39'},
							{value:d.Age6, name:'40到49'},
							{value:d.Age7, name:'大于50'}
						]
					},
					{
						name:'gender',
			            type:'pie',
			            radius: [0, '40%'],
						data:[
							{value: d.male, name:'男', selected:true},
							{value: d.female, name:'女'},
							{value: d.gunknown, name:'未知'}
						]
					}
				]
				});
			}
		});
	});
	monChart.on("click",function(par){
		var mon;
		switch (par.data.name){
			case 'May':
				mon='5';
				break;
			case 'Jun':
				mon='6';
				break;
            case 'Jly':
                mon='7';
                break;
            case 'Aug':
                mon='8';
                break;
            case 'Sep':
                mon='9';
                break;
            case 'Oct':
                mon='10';
                break;
            case 'Nov':
                mon='11';
                break;
			default:
				break;
		}
		$.ajax({
			type:'GET',
			url:'certainMonth.do?month='+mon,//String eg:'5'
			dataType:'json',
			success:function(d){//返回统计值（数字）
				//{"age1":xx,"age2":xx,.....}
				mainChart.setOption({
					legend: {
			        data:['女','男','小于18','18到24','25到29','30到34','35到39','40到49','大于50','未知']
			    },
				series:[
					{
						name:'age',
			            type:'pie',
			            radius: ['60%', '85%'],
						data:[
							{value:d.Age1, name:'小于18'},
							{value:d.Age2, name:'18到24'},
							{value:d.Age3, name:'25到29'},
							{value:d.Age4, name:'30到34'},
							{value:d.Age5, name:'35到39'},
							{value:d.Age6, name:'40到49'},
							{value:d.Age7, name:'大于50'}
						]
					},
					{
						name:'gender',
			            type:'pie',
			            radius: [0, '40%'],
						data:[
							{value: d.male, name:'男', selected:true},
							{value: d.female, name:'女'},
							{value: d.gunknown, name:'未知'}
						]
					}
				]
				});
			}
		});
	});
}