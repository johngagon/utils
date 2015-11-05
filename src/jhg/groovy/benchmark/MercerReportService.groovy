package jhg.groovy.benchmark

import java.util.*;

class MercerReportService {

	def dataInput
	
	def rptDetailInC
	def rptDetailAll
	def generalSummary
	def rptSummaryTotal
	def rptSummaryIP
	def rptSummaryOP
	def rptSummaryPR
	
	
	def inFieldHeader
	
	public MercerReportService() {
		dataInput = []
		rptDetailInC = []
		rptDetailAll = []
		rptSummaryTotal = []
		rptSummaryIP = []
		rptSummaryOP = []
		rptSummaryPR = []
		//stdDetailInC = []
		inFieldHeader = []
		generalSummary = []
	}

	def test(){
		def dataInputRsc 			= "./data/mercer/mercer_input.txt"

		inFieldHeader = ['area','state','percent_enr','vendortoprint','ipelig','opelig','prelig','total_volume','inn_std_ip','inn_std_op','inn_std_pr','ic_percent_ip','ic_percent_op','ic_percent_pr','ic_percent_all_std','ic_percent_all_bob','ic_discount_e_ip','ic_discount_e_op','ic_discount_e_pr','ic_discount_e_std','ic_discount_e_std_rel','ic_discount_b_ip','ic_discount_b_op','ic_discount_b_pr','ic_discount_b_std','ic_discount_b_std_rel','overall_ip_discount','overall_op_discount','overall_pr_discount','overall_discount_std','overall_discount_std_rel','ip_per_diem','ip_alos','lab_disc','rad_disc']
		readFile(dataInputRsc)
		process()
		debug()
	}
	
	def debug(){
		rptDetailInC.each{row->
			println "${row}"
		}
	}
	
	def readFile(def dataInputRsc){
		println "Reading Files Data Input:${dataInputRsc}"
		final int MAX_REC = 15;
		
		File file = new File(dataInputRsc)
		if(file.exists()){
			println "File found."
			
			file.eachLine(){ line, i ->
				if(i<=MAX_REC){
					def list = []
					inFieldHeader.size()
					line.split(/\t/,-1).eachWithIndex{ val, j ->
						list << val
					}//split
					
					def map = [inFieldHeader, list].transpose().inject([:]) { a, b -> a[b[0]] = b[1]; a }
					dataInput << map
				}
			}//eachLine
			
			println ""
			

		}//if(file.exists())
		else{
			println "Input File not found."
		}
		println "DataInput : ${dataInput.size()} records read."
		println "Reading complete."
		println "Processing raw results for this test..."
	}
	
	def process(){
		def totalRowCarrier = 'Market Total ($) or Average (%)'
		def blueCard = 'Blue Card'
		def blinded = 'Blinded Carrier'
		def dollarUnit = 1000;
	
		def groupRow = []
		def allGroupRow = []
		dataInput.eachWithIndex { row, i ->   //row = [:]
			
			//println "${row}"
			
				
			if(totalRowCarrier.equals(row.vendortoprint)){
				//row is totals row
				def sum = 0.0
				def grpCount = 0
				
				def nonBlueSum = 0.0
				def nonBlueCount = 0
				
				def compGrpSumIP = 0.0
				def compGrpSumOP = 0.0
				def compGrpSumPR = 0.0
				def nonBcbsSumIP = 0.0
				def nonBcbsSumOP = 0.0
				def nonBcbsSumPR = 0.0
				List<Double> totalDiscountRanks = new ArrayList<Double>();
				List<Double> ipDiscountRanks = new ArrayList<Double>();
				List<Double> opDiscountRanks = new ArrayList<Double>();
				List<Double> prDiscountRanks = new ArrayList<Double>();
				def topTotalCompetitorDiscount = 0.0
				def topIpCompetitorDiscount = 0.0
				def topOpCompetitorDiscount = 0.0
				def topPrCompetitorDiscount = 0.0
				
				groupRow.each { gr ->
					sum += gr.planDiscount
					totalDiscountRanks.add(gr.planDiscount)
					
					compGrpSumIP += gr.planDiscountInpatient
					ipDiscountRanks.add(gr.planDiscountInpatient)
					
					compGrpSumOP += gr.planDiscountOutpatient
					opDiscountRanks.add(gr.planDiscountOutpatient)
					
					compGrpSumPR += gr.planDiscountProfessional
					prDiscountRanks.add(gr.planDiscountProfessional)
					
					grpCount ++
					
					if(!gr.isBlue){
						if(gr.planDiscount > topTotalCompetitorDiscount){
							topTotalCompetitorDiscount = gr.planDiscount
						}
						if(gr.planDiscountInpatient > topIpCompetitorDiscount){
							topIpCompetitorDiscount = gr.planDiscountInpatient
						}
						if(gr.planDiscountOutpatient > topOpCompetitorDiscount){
							topOpCompetitorDiscount = gr.planDiscountOutpatient
						}
						if(gr.planDiscountProfessional > topPrCompetitorDiscount){
							topPrCompetitorDiscount = gr.planDiscountProfessional
						}
						nonBlueSum += gr.planDiscount
						nonBcbsSumIP += gr.planDiscountInpatient
						nonBcbsSumOP += gr.planDiscountOutpatient
						nonBcbsSumPR += gr.planDiscountProfessional
						nonBlueCount++
					}

				}
				
				Collections.sort(totalDiscountRanks,Collections.reverseOrder());
				Collections.sort(ipDiscountRanks,Collections.reverseOrder());
				Collections.sort(opDiscountRanks,Collections.reverseOrder());
				Collections.sort(prDiscountRanks,Collections.reverseOrder());
				if(totalDiscountRanks.size() >10)totalDiscountRanks = totalDiscountRanks.subList(0, 9)
				if(ipDiscountRanks.size() >10){ipDiscountRanks = ipDiscountRanks.subList(0, 9)}
				if(opDiscountRanks.size() >10){opDiscountRanks = opDiscountRanks.subList(0, 9)}
				if(prDiscountRanks.size() >10){prDiscountRanks = prDiscountRanks.subList(0, 9)}
				
				groupRow.each { gr ->
					gr.compGroupAvg = sum/grpCount
					gr.nonBcbsAvg = nonBlueSum / nonBlueCount
					gr.compGroupAvgInpatient = compGrpSumIP/grpCount
					gr.compGroupAvgOutpatient = compGrpSumOP/grpCount
					gr.compGroupAvgProfessional = compGrpSumPR/grpCount
					gr.nonBcbsAvgInpatient = nonBcbsSumIP/nonBlueCount
					gr.nonBcbsAvgOutpatient = nonBcbsSumOP/nonBlueCount
					gr.nonBcbsAvgProfessional = nonBcbsSumPR/nonBlueCount
					
					if(gr.isBlue){
						
						gr.marketClaims = row.total_volume * dollarUnit
						gr.planClaimsPct = (gr.planClaims as Double) / (gr.marketClaims as Double)
						
						gr.marketClaimsInpatient = row.ipelig
						gr.marketClaimsOutpatient = row.opelig
						gr.marketClaimsProfessional = row.prelig
						gr.planClaimsPctInpatient = gr.planClaimsInpatient / (gr.marketClaimsInpatient as Double)
						gr.planClaimsPctOutpatient = gr.planClaimsOutpatient / (gr.marketClaimsOutpatient as Double) 
						gr.planClaimsPctProfessional = gr.planClaimsProfessional / (gr.marketClaimsProfessional as Double)
						gr.discountCarriersInMarket = groupRow.size()
						gr.ttranks = totalDiscountRanks
						gr.ipranks = ipDiscountRanks
						gr.opranks = opDiscountRanks
						gr.prranks = prDiscountRanks
						gr.discountMeasure = 'IC'	
						gr.ttBcbsRank = totalDiscountRanks.indexOf(gr.planDiscount) +1 
						gr.ipBcbsRank = ipDiscountRanks.indexOf(gr.planDiscountInpatient) +1
						gr.opBcbsRank = opDiscountRanks.indexOf(gr.planDiscountOutpatient) +1
						gr.prBcbsRank = prDiscountRanks.indexOf(gr.planDiscountProfessional) +1
						gr.ttCompetAveDiff = gr.planDiscount - gr.nonBcbsAvg
						gr.ipCompetAveDiff  = gr.planDiscountInpatient - gr.nonBcbsAvgInpatient
						gr.opCompetAveDiff  = gr.planDiscountOutpatient - gr.nonBcbsAvgOutpatient
						gr.prCompetAveDiff  = gr.planDiscountProfessional - gr.nonBcbsAvgProfessional
						gr.ttDiffBestCompet = gr.planDiscount - topTotalCompetitorDiscount
						gr.ipDiffBestCompet  = gr.planDiscountInpatient - topIpCompetitorDiscount
						gr.opDiffBestCompet  = gr.planDiscountOutpatient - topOpCompetitorDiscount
						gr.prDiffBestCompet  = gr.planDiscountProfessional - topPrCompetitorDiscount
						generalSummary <<gr
					}
					
					rptDetailInC << gr
					
				}
				
				
				allGroupRow = []
				sum = 0.0
				grpCount = 0
				
				nonBlueSum = 0.0
				nonBlueCount = 0
				
				compGrpSumIP = 0.0
				compGrpSumOP = 0.0
				compGrpSumPR = 0.0
				nonBcbsSumIP = 0.0
				nonBcbsSumOP = 0.0
				nonBcbsSumPR = 0.0
				totalDiscountRanks = new ArrayList<Double>();
				ipDiscountRanks = new ArrayList<Double>();
				opDiscountRanks = new ArrayList<Double>();
				prDiscountRanks = new ArrayList<Double>();
				
				allGroupRow.each { gr ->
					sum += gr.planDiscount
					totalDiscountRanks.add(gr.planDiscount)
					
					compGrpSumIP += gr.planDiscountInpatient
					ipDiscountRanks.add(gr.planDiscountInpatient)
					
					compGrpSumOP += gr.planDiscountOutpatient
					opDiscountRanks.add(gr.planDiscountOutpatient)
					
					compGrpSumPR += gr.planDiscountProfessional
					prDiscountRanks.add(gr.planDiscountProfessional)
					
					grpCount ++
					
					if(!gr.isBlue){
						if(gr.planDiscount > topTotalCompetitorDiscount){
							topTotalCompetitorDiscount = gr.planDiscount
						}
						if(gr.planDiscountInpatient > topIpCompetitorDiscount){
							topIpCompetitorDiscount = gr.planDiscountInpatient
						}
						if(gr.planDiscountOutpatient > topOpCompetitorDiscount){
							topOpCompetitorDiscount = gr.planDiscountOutpatient
						}
						if(gr.planDiscountProfessional > topPrCompetitorDiscount){
							topPrCompetitorDiscount = gr.planDiscountProfessional
						}
						nonBlueSum += gr.planDiscount
						nonBcbsSumIP += gr.planDiscountInpatient
						nonBcbsSumOP += gr.planDiscountOutpatient
						nonBcbsSumPR += gr.planDiscountProfessional
						nonBlueCount++
					}

				}
				Collections.sort(totalDiscountRanks,Collections.reverseOrder());
				Collections.sort(ipDiscountRanks,Collections.reverseOrder());
				Collections.sort(opDiscountRanks,Collections.reverseOrder());
				Collections.sort(prDiscountRanks,Collections.reverseOrder());
				if(totalDiscountRanks.size() >10){totalDiscountRanks = totalDiscountRanks.subList(0, 9)}
				if(ipDiscountRanks.size() >10)   {ipDiscountRanks = ipDiscountRanks.subList(0, 9)}
				if(opDiscountRanks.size() >10)   {opDiscountRanks = opDiscountRanks.subList(0, 9)}
				if(prDiscountRanks.size() >10)   {prDiscountRanks = prDiscountRanks.subList(0, 9)}
				
				allGroupRow.each { gr ->
					gr.compGroupAvg = sum/grpCount
					gr.nonBcbsAvg = nonBlueSum / nonBlueCount
					gr.compGroupAvgInpatient = compGrpSumIP/grpCount
					gr.compGroupAvgOutpatient = compGrpSumOP/grpCount
					gr.compGroupAvgProfessional = compGrpSumPR/grpCount
					gr.nonBcbsAvgInpatient = nonBcbsSumIP/nonBlueCount
					gr.nonBcbsAvgOutpatient = nonBcbsSumOP/nonBlueCount
					gr.nonBcbsAvgProfessional = nonBcbsSumPR/nonBlueCount
					if(gr.isBlue){
						
						gr.marketClaims = row.total_volume * dollarUnit
						gr.planClaimsPct = gr.planClaims / gr.marketClaims
						
						gr.marketClaimsInpatient = row.ipelig
						gr.marketClaimsOutpatient = row.opelig
						gr.marketClaimsProfessional = row.prelig
						gr.planClaimsPctInpatient = gr.planClaimsInpatient / (gr.marketClaimsInpatient as Double)
						gr.planClaimsPctOutpatient = gr.planClaimsOutpatient / (gr.marketClaimsOutpatient as Double) 
						gr.planClaimsPctProfessional = gr.planClaimsProfessional / (gr.marketClaimsProfessional as Double)
						
						gr.discountCarriersInMarket = allGroupRow.size()
						
						gr.ttranks = totalDiscountRanks
						gr.ipranks = ipDiscountRanks
						gr.opranks = opDiscountRanks
						gr.prranks = prDiscountRanks
						gr.discountMeasure = 'Total'	
						gr.ttBcbsRank = totalDiscountRanks.indexOf(gr.planDiscount) +1 
						gr.ipBcbsRank = ipDiscountRanks.indexOf(gr.planDiscountInpatient) +1
						gr.opBcbsRank = opDiscountRanks.indexOf(gr.planDiscountOutpatient) +1
						gr.prBcbsRank = prDiscountRanks.indexOf(gr.planDiscountProfessional) +1
						gr.ttCompetAveDiff = gr.planDiscount - gr.gr.nonBcbsAvg
						gr.ipCompetAveDiff  = gr.planDiscountInpatient - gr.nonBcbsAvgInpatient
						gr.opCompetAveDiff  = gr.planDiscountOutpatient - gr.nonBcbsAvgOutpatient
						gr.prCompetAveDiff  = gr.planDiscountProfessional - gr.nonBcbsAvgProfessional
						gr.ttDiffBestCompet = gr.planDiscount - topTotalCompetitorDiscount
						gr.ipDiffBestCompet  = gr.planDiscountInpatient - topIpCompetitorDiscount
						gr.opDiffBestCompet  = gr.planDiscountOutpatient - topOpCompetitorDiscount
						gr.prDiffBestCompet  = gr.planDiscountProfessional - topPrCompetitorDiscount
						generalSummary <<gr
					}
					
					rptDetailAll << gr
					
				}
				allGroupRow = []
				
				
			}else{
			
			
				def dric = [:]
				def drall = [:]
				dric.state = row.state
				drall.state = row.state
				
				dric.market = row.area
				drall.market = row.area
				
				dric.carrierName = row.vendortoprint
				drall.carrierName = row.vendortoprint
				
				dric.isBlue = !blinded.equals(row.vendortoprint)
				drall.isBlue = !blinded.equals(row.vendortoprint)
				
				dric.networkType = dric.isBlue?blueCard:''
				drall.networkType = drall.isBlue?blueCard:''
				 
				dric.planDiscount = row.ic_discount_e_std as Double
				drall.planDiscount = row.overall_discount_std as Double
				
				dric.planDiscountInpatient = row.ic_discount_e_ip as Double
				drall.planDiscountInpatient = row.overall_ip_discount as Double
				
				dric.planDiscountOutpatient = row.ic_discount_e_op as Double
				drall.planDiscountOutpatient = row.overall_op_discount as Double
				
				dric.planDiscountProfessional = row.ic_discount_e_pr as Double
				drall.planDiscountProfessional = row.overall_pr_discount as Double
				
				if(dric.isBlue){
					dric.planClaims = Integer.valueOf(row.total_volume) * dollarUnit
					drall.planClaims = (Integer.valueOf(row.total_volume) * dollarUnit) / (row.ic_percent_all_std as Double)
					
					dric.planClaimsInpatient = Integer.valueOf(row.ipelig) * dollarUnit
					drall.planClaimsInpatient = (Integer.valueOf(row.ipelig) * dollarUnit) / (row.ic_percent_ip as Double)
					
					dric.planClaimsOutpatient = Integer.valueOf(row.opelig) * dollarUnit
					drall.planClaimsOutpatient = (Integer.valueOf(row.opelig) * dollarUnit) / (row.ic_percent_op as Double)
					
					dric.planClaimsProfessional = Integer.valueOf(row.prelig) * dollarUnit
					drall.planClaimsProfessional = (Integer.valueOf(row.prelig) * dollarUnit) / (row.ic_percent_pr as Double)
				}
				groupRow << dric
				allGroupRow << drall
			}//else
			// In-Network Billed divided by the penetration percent
			
		
			
		}//dataInput.eachWithIndex
		
	}//process
	
	def processSummary(){
		
		def totalRec = [:]
		def ipRec = [:]
		def opRec = [:]
		def prRec = [:]

		generalSummary.each{ row ->
			processSubSummary(row,'Total',totalRec)
			processSubSummary(row,'Inpatient',ipRec)
			processSubSummary(row,'Outpatient',opRec)
			processSubSummary(row,'Professional',prRec)
			processRanks(row.ttranks,totalRec)
			processRanks(row.ipranks,ipRec)
			processRanks(row.opranks,opRec)
			processRanks(row.prranks,prRec)
			totalRec.discountBlueRank = row.ttBcbsRank
			ipRec.discountBlueRank    = row.ipBcbsRank
			opRec.discountBlueRank    = row.opBcbsRank
			prRec.discountBlueRank    = row.prBcbsRank
			totalRec.discountCarriersInMarket = row.discountCarriersInMarket
			ipRec.discountCarriersInMarket    = row.discountCarriersInMarket
			opRec.discountCarriersInMarket    = row.discountCarriersInMarket
			prRec.discountCarriersInMarket    = row.discountCarriersInMarket
			totalRec.discountNonBcbsAvg = row.nonBcbsAvg
			ipRec.discountNonBcbsAvg    = row.nonBcbsAvgInpatient
			opRec.discountNonBcbsAvg    = row.nonBcbsAvgOutpatient
			prRec.discountNonBcbsAvg    = row.nonBcbsAvgProfessional
			totalRec.discountBlueAvgDiff = row.ttCompetAveDiff
			ipRec.discountBlueAvgDiff    = row.ipCompetAveDiff
			opRec.discountBlueAvgDiff    = row.opCompetAveDiff
			prRec.discountBlueAvgDiff    = row.prCompetAveDiff
			totalRec.discountBlueBestDiff = row.ttDiffBestCompet
			ipRec.discountBlueBestDiff    = row.ipDiffBestCompet
			opRec.discountBlueBestDiff    = row.opDiffBestCompet
			prRec.discountBlueBestDiff    = row.prDiffBestCompet
			
		}
		
	}

	def processSubSummary(def row, def type, def rec){
		rec.type = type
		rec.state = row.state
		rec.market = row.market
		rec.carrier = row.carrier
		rec.networkType = row.networkType
		rec.discountMeasure = row.discountMeasure
		rec.total
	}
	def processRanks(def rowRankList, def rec ){
		for(int i=0;i<10;i++){
			rec."discount_${i+1}" = rowRankList.get(i)?:''
		}
	}
	
	/*
	 * Run as Groovy Script
	 */
	public static void main(String[] args){
		println "Start."
		MercerReportService svc = new MercerReportService();
		svc.test();
		println "End."
		
	}
}
/*
				//Validation post split in readFile
				if(list.size()!=inFieldHeader.size()){
					if(c<=3){
						println "Parse error line: ${i}, Expected fieldcount: ${inFieldHeader.size()} Received fieldcount: ${list.size()}    Line:${line}  "
						inFieldHeader.eachWithIndex{ header, x ->
							println "Header(${x}):${header}"
						}
						list.eachWithIndex{ listval, y ->
							println "Value (${y}):${listval}"
						}
							
					}else{
						print "${i}, "
					}
					c++
				}else{
					def map = [inFieldHeader, list].transpose().inject([:]) { a, b -> a[b[0]] = b[1]; a }
					dataInput << map
				}
				
 */

//def readFile(def dataInputRsc){
//InputStream dataInputStream
//dataInputStream = this.class.classLoader.getResourceAsStream(dataInputRsc)
//if(dataInputStream){
//	dataInputStream.eachLine(){ line ->
		
		//def list = []
		
		//line.split(/\t/,-1).eachWithIndex{ val, i ->
			/*
			if(booleanIndexes.contains(i)){
				list << (('t'.equals(val)) as Boolean)
			}else if(longIndexes.contains(i)){
				list << ((val?:0) as Long)
			}else if(doubleIndexes.contains(i)){
				list << ((val?:0.0) as Double)
			}else{
			*/
				//list << val
			/*
			}
			*/
		//}//split
		//def map = [inFieldHeader, list].transpose().inject([:]) { a, b -> a[b[0]] = b[1]; a }
		//dataInput << map
		//println "${map}"
	//}//eachLine
	
//}else{ //if(dataInputStream)
//	println "Could not get input stream."
//}//else:!if(dataInputStream)

//println "  Data Input    size(): ${dataInput.size()}"
//}

/*
 ['area','state','percent_enr','vendortoprint',
 'ipelig','opelig','prelig','total_volume',
 'inn_std_ip','inn_std_op','inn_std_pr',
 'ic_percent_ip','ic_percent_op','ic_percent_pr','ic_percent_all_std','ic_percent_all_bob',
 'ic_discount_e_ip','ic_discount_e_op','ic_discount_e_pr','ic_discount_e_std','ic_discount_e_std_rel',
 'ic_discount_b_ip','ic_discount_b_op','ic_discount_b_pr','ic_discount_b_std','ic_discount_b_std_rel',
 'overall_ip_discount','overall_op_discount','overall_pr_discount','overall_discount_std','overall_discount_std_rel',
 'ip_per_diem','ip_alos','lab_disc','rad_disc']
 
 
			  /
 */
//"C:\\Programming Tools\\eclipse\\workspace\\GroovyHelloWorld\\resource\\test.txt"

//def expectedDetailIncRsc 	= "data/mercer/mercer_dr_inc.txt"
//def expectedDetailAllRsc 	= "data/mercer/mercer_dr_all.txt"
//def expectedSummaryRsc 		= "data/mercer/mercer_dr_all.txt"