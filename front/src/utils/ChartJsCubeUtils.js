import ChartJs from "chart.js"; 

var defaultPalettes = [
	'rgb(255, 99, 132)',
	'rgb(255, 159, 64)',
	'rgb(255, 205, 86)',
	'rgb(75, 192, 192)',
	'rgb(54, 162, 235)',
	'rgb(153, 102, 255)',
	'rgb(201, 203, 207)'
];

var borderPalettes = [
	'rgb(235, 79, 112)',
	'rgb(235, 139, 44)',
	'rgb(235, 185, 66)',
	'rgb(55, 172, 172)',
	'rgb(34, 142, 215)',
	'rgb(133, 82, 235)',
	'rgb(181, 183, 187)'
];

function buildDiagramBar(canvasElement, cubeCells, retrieveLabelName, retrieveGroupName) {
    var labelsIndex = {};
    var labels = [];  
        
    var datasetsOrders = [];
    var datasetsValues = {};

    console.log(cubeCells);

    // Création d'un liste trié
    cubeCells.forEach((c)=>{
        var labelName = retrieveLabelName(c);
        if(labelsIndex[labelName] == undefined) {
            labelsIndex[labelName] = labels.length;            
            labels.push(labelName);
        }
    });

    labels.sort((a,b)=>a.localeCompare(b));
    labels.forEach((l,idx)=>labelsIndex[l] = idx);
    
    cubeCells.forEach((c)=>{
        var labelName = retrieveLabelName(c);
        var groupName = retrieveGroupName(c);
    
        var values = datasetsValues[groupName];
        if(values == undefined) {
            values= [];

            var color  = defaultPalettes[datasetsOrders.length%defaultPalettes.length];
            var borderColor = borderPalettes[datasetsOrders.length%borderPalettes.length];
            datasetsOrders.push({
                label: groupName,
                data: values,
                backgroundColor: color,						
                borderColor: borderColor,						
                borderWidth: 1					
            });
            datasetsValues[groupName] = values;
        }
        
        var sum  = values[labelsIndex[labelName]];
        if(! sum) {
            sum = c.VALUE;
        } else {
            sum += c.VALUE;
        }
        values[labelsIndex[labelName]] = sum;

    });
    

    datasetsOrders.sort((a,b)=>a.label.localeCompare(b.label));
    

    var ctx = canvasElement.getContext('2d');
    return new ChartJs(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: datasetsOrders            
        },
        options: {
             scales: {
                  yAxes: [{
                        display: true,
                        ticks: {
                            beginAtZero: true,                                                        
                        }
                    }]
            },
        }
    });  	
}

export default buildDiagramBar;