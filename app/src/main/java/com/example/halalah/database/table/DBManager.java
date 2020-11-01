package com.example.halalah.database.table;

import android.content.Context;

import com.example.halalah.POS_MAIN;
import com.example.halalah.PosApplication;
import com.example.halalah.TMS.AID_Data;
import com.example.halalah.TMS.Public_Key;
import com.example.halalah.TMS.TMSManager;
import com.example.halalah.qrcode.utils.SDKLog;
import com.topwise.cloudpos.struct.BytesUtil;
import com.topwise.cloudpos.struct.TlvList;

public final class DBManager {
	private static DBManager instance = new DBManager();
	private AidDaoImpl aidDao = null;
	private CapkDaoImpl capkDao = null;
	private boolean hasinit = false;
	
	private DBManager() {
	}
	
	public static DBManager getInstance() {
		return instance;
	}

	/** function init
	 \function Name: init
	 \Param  : Context
	 \Return : void
	 \Pre    :
	 \Post   :
	 \Author	: Mostafa Hussiny
	 \DT		: 8/00/2020
	 \Des    : initiate and save aid and capk data
	 */

	public void init(Context context) {
		if (hasinit)
			return;
		hasinit = true;
		initDaoImpl(context);

	/*	Aid aid = new Aid((byte)0x00);
		TlvList list = new TlvList();
		list.fromHex("9F0607A0000005241010DF0101009F08020140DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400006000DF150400000000DF160199DF170199DF14039F3704DF180100DF1906000000003000DF2006000000010000DF2106000000007500");
		aid.fromTlvList(list);
		aidDao.insert(aid);*/
/*
		aid = new Aid((byte)0x00); //mada
		list = new TlvList();
		list.fromHex("9F0607A0000002282010DF0101009F08020084DF1105FC408CA800DF1205FC408CF800DF130500100000009F1B0400006000DF150400000290DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000003000DF2006000000010000DF2106000000007500");
		aid.fromTlvList(list);
		aidDao.insert(aid);
		aid = new Aid((byte)0x00); //mada
		list = new TlvList();
		list.fromHex("9F0607A0000002281010DF0101009F08020002DF1105FC408CA800DF1205FC408CF800DF130500100000009F1B0400006000DF150400000290DF14039F3704DF1801019F7B06000000100000DF1906000000003000DF2006000000010000DF2106000000007500");
		aid.fromTlvList(list);
		aidDao.insert(aid);
		aid = new Aid((byte)0x00);
		list = new TlvList();
		list.fromHex("9F0607A0000000031010DF0101009F08020140DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400006000DF150400000000DF160199DF170199DF14039F3704DF180100DF1906000000003000DF2006000000010000DF2106000000007500");
		aid.fromTlvList(list);
		aidDao.insert(aid);
		aid = new Aid((byte)0x00);
		list = new TlvList();
		list.fromHex("9F0607A0000000032010DF0101009F08020140DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400006000DF150400000000DF160199DF170199DF14039F3704DF180100DF1906000000003000DF2006000000010000DF2106000000007500");
		aid.fromTlvList(list);
		aidDao.insert(aid);
		aid = new Aid((byte)0x00);
		list = new TlvList();
		list.fromHex("9F0607A0000000033010DF0101009F08020140DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400006000DF150400000000DF160199DF170199DF14039F3704DF180100DF1906000000003000DF2006000000010000DF2106000000007500");
		aid.fromTlvList(list);
		aidDao.insert(aid);
		aid = new Aid((byte)0x00);
		list = new TlvList();
		list.fromHex("9F0607A0000000041010DF0101009F08020002DF1105FC5080A000DF1205F85080F800DF130504000000009F1B0400006000DF150400000000DF160199DF170199DF14039F3704DF180100DF1906000000003000DF2006000000010000DF2106000000007500");
		aid.fromTlvList(list);
		aidDao.insert(aid);
		aid = new Aid((byte)0x00);
		list = new TlvList();
		list.fromHex("9F0607A0000000043060DF0101009F08020002DF1105FC5058A000DF1205F85058F800DF130504000000009F1B0400006000DF150400000000DF160199DF170199DF14039F3704DF180101DF1906000000003000DF2006000000010000DF2106000000007500");
		aid.fromTlvList(list);
		aidDao.insert(aid);
		aid = new Aid((byte)0x00);
		list = new TlvList();
		list.fromHex("9F0607A0000000651010DF0101009F08020200DF1105FC6024A800DF1205FC60ACF800DF130500100000009F1B0400006000DF150400000000DF160199DF170199DF14039F3704DF180100DF1906000000003000DF2006000000010000DF2106000000007500");
		aid.fromTlvList(list);
		aidDao.insert(aid);
		aid = new Aid((byte)0x00);
		list = new TlvList();
		list.fromHex("9F0608A000000333010101DF0101009F08020030DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400006000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000003000DF2006000000010000DF2106000000007500");
		aid.fromTlvList(list);
		aidDao.insert(aid);
		aid = new Aid((byte)0x00);
		list = new TlvList();
		list.fromHex("9F0608A000000333010102DF0101009F08020030DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400006000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000003000DF2006000000010000DF2106000000007500");
		aid.fromTlvList(list);
		aidDao.insert(aid);
		aid = new Aid((byte)0x00);
		list = new TlvList();
		list.fromHex("9F0608A000000333010103DF0101009F08020030DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400006000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000000010000DF2106000000007500");
		aid.fromTlvList(list);
		aidDao.insert(aid);
		aid = new Aid((byte)0x00);
		list = new TlvList();
		list.fromHex("9F0608A000000333010106DF0101009F08020030DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400006000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000000010000DF2106000000007500");
		aid.fromTlvList(list);
		aidDao.insert(aid);
		aid = new Aid((byte)0x00);
		list = new TlvList();
		list.fromHex("9F0607A0000003330101DF0101019F08020030DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400006000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000000010000DF2106000000007500");
		aid.fromTlvList(list);
		aidDao.insert(aid);
		aid = new Aid((byte)0x00);
		list = new TlvList();
		list.fromHex("9F0606A00000002501DF0101009F08020002DF1105FC5080A000DF1205F85080F800DF130500100000009F1B0400006000DF150400000000DF160199DF170199DF14039F3704DF180100DF1906000000030000DF2006000000010000DF2106000000007500");
		aid.fromTlvList(list);
		aidDao.insert(aid);*/


/*

//todo remove comment when TMS is enabled
		AID_Data AIDdata[] = TMSManager.getInstance().getAidDataList().toArray(new AID_Data[0]);
		if(AIDdata.length>0)
		{
			for (int i = 0; i < AIDdata.length; i++) {
				String sAIDdata = POS_MAIN.FormatAIDData(AIDdata[i]);
				aid = new Aid((byte) 0x00);
				list = new TlvList();
				list.fromHex(sAIDdata);
				aid.fromTlvList(list);
				aidDao.insert(aid);

			}
		}
*/


/*
		//JCB CAPK
		Capk capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000000659F220109DF05083230303931323331DF060101DF070101DF028180B72A8FEF5B27F2B550398FDCC256F714BAD497FF56094B7408328CB626AA6F0E6A9DF8388EB9887BC930170BCC1213E90FC070D52C8DCD0FF9E10FAD36801FE93FC998A721705091F18BC7C98241CADC15A2B9DA7FB963142C0AB640D5D0135E77EBAE95AF1B4FEFADCF9C012366BDDA0455C1564A68810D7127676D493890BDDF040103DF03144410C6D51C2F83ADFD92528FA6E38A32DF048D0A");
		capk.fromTlvList(list);
		capkDao.insert(capk);*/
/*
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000000659F220110DF05083230313231323331DF060101DF070101DF02819099B63464EE0B4957E4FD23BF923D12B61469B8FFF8814346B2ED6A780F8988EA9CF0433BC1E655F05EFA66D0C98098F25B659D7A25B8478A36E489760D071F54CDF7416948ED733D816349DA2AADDA227EE45936203CBF628CD033AABA5E5A6E4AE37FBACB4611B4113ED427529C636F6C3304F8ABDD6D9AD660516AE87F7F2DDF1D2FA44C164727E56BBC9BA23C0285DF040103DF0314C75E5210CBE6E8F0594A0F1911B07418CADB5BAB");
		capk.fromTlvList(list);
		capkDao.insert(capk);
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000000659F220112DF05083230313431323331DF060101DF070101DF0281B0ADF05CD4C5B490B087C3467B0F3043750438848461288BFEFD6198DD576DC3AD7A7CFA07DBA128C247A8EAB30DC3A30B02FCD7F1C8167965463626FEFF8AB1AA61A4B9AEF09EE12B009842A1ABA01ADB4A2B170668781EC92B60F605FD12B2B2A6F1FE734BE510F60DC5D189E401451B62B4E06851EC20EBFF4522AACC2E9CDC89BC5D8CDE5D633CFD77220FF6BBD4A9B441473CC3C6FEFC8D13E57C3DE97E1269FA19F655215B23563ED1D1860D8681DF040103DF0314874B379B7F607DC1CAF87A19E400B6A9E25163E8");
		capk.fromTlvList(list);
		capkDao.insert(capk);
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000000659F220114DF05083230313631323331DF060101DF070101DF0281F8AEED55B9EE00E1ECEB045F61D2DA9A66AB637B43FB5CDBDB22A2FBB25BE061E937E38244EE5132F530144A3F268907D8FD648863F5A96FED7E42089E93457ADC0E1BC89C58A0DB72675FBC47FEE9FF33C16ADE6D341936B06B6A6F5EF6F66A4EDD981DF75DA8399C3053F430ECA342437C23AF423A211AC9F58EAF09B0F837DE9D86C7109DB1646561AA5AF0289AF5514AC64BC2D9D36A179BB8A7971E2BFA03A9E4B847FD3D63524D43A0E8003547B94A8A75E519DF3177D0A60BC0B4BAB1EA59A2CBB4D2D62354E926E9C7D3BE4181E81BA60F8285A896D17DA8C3242481B6C405769A39D547C74ED9FF95A70A796046B5EFF36682DC29DF040103DF0314C0D15F6CD957E491DB56DCDD1CA87A03EBE06B7B");
		capk.fromTlvList(list);
		capkDao.insert(capk);

		//China Unionpay CAPK
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000003339F220101DF05083230303931323331DF060101DF070101DF028180BBE9066D2517511D239C7BFA77884144AE20C7372F515147E8CE6537C54C0A6A4D45F8CA4D290870CDA59F1344EF71D17D3F35D92F3F06778D0D511EC2A7DC4FFEADF4FB1253CE37A7B2B5A3741227BEF72524DA7A2B7B1CB426BEE27BC513B0CB11AB99BC1BC61DF5AC6CC4D831D0848788CD74F6D543AD37C5A2B4C5D5A93BDF040103DF0314E881E390675D44C2DD81234DCE29C3F5AB2297A0");
		capk.fromTlvList(list);
		capkDao.insert(capk);
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000003339F220102DF05083230313431323331DF060101DF070101DF028190A3767ABD1B6AA69D7F3FBF28C092DE9ED1E658BA5F0909AF7A1CCD907373B7210FDEB16287BA8E78E1529F443976FD27F991EC67D95E5F4E96B127CAB2396A94D6E45CDA44CA4C4867570D6B07542F8D4BF9FF97975DB9891515E66F525D2B3CBEB6D662BFB6C3F338E93B02142BFC44173A3764C56AADD202075B26DC2F9F7D7AE74BD7D00FD05EE430032663D27A57DF040103DF031403BB335A8549A03B87AB089D006F60852E4B8060");
		capk.fromTlvList(list);
		capkDao.insert(capk);
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000003339F220103DF05083230313731323331DF060101DF070101DF0281B0B0627DEE87864F9C18C13B9A1F025448BF13C58380C91F4CEBA9F9BCB214FF8414E9B59D6ABA10F941C7331768F47B2127907D857FA39AAF8CE02045DD01619D689EE731C551159BE7EB2D51A372FF56B556E5CB2FDE36E23073A44CA215D6C26CA68847B388E39520E0026E62294B557D6470440CA0AEFC9438C923AEC9B2098D6D3A1AF5E8B1DE36F4B53040109D89B77CAFAF70C26C601ABDF59EEC0FDC8A99089140CD2E817E335175B03B7AA33DDF040103DF031487F0CD7C0E86F38F89A66F8C47071A8B88586F26");
		capk.fromTlvList(list);
		capkDao.insert(capk);
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000003339F220104DF05083230313731323331DF060101DF070101DF0281F8BC853E6B5365E89E7EE9317C94B02D0ABB0DBD91C05A224A2554AA29ED9FCB9D86EB9CCBB322A57811F86188AAC7351C72BD9EF196C5A01ACEF7A4EB0D2AD63D9E6AC2E7836547CB1595C68BCBAFD0F6728760F3A7CA7B97301B7E0220184EFC4F653008D93CE098C0D93B45201096D1ADFF4CF1F9FC02AF759DA27CD6DFD6D789B099F16F378B6100334E63F3D35F3251A5EC78693731F5233519CDB380F5AB8C0F02728E91D469ABD0EAE0D93B1CC66CE127B29C7D77441A49D09FCA5D6D9762FC74C31BB506C8BAE3C79AD6C2578775B95956B5370D1D0519E37906B384736233251E8F09AD79DFBE2C6ABFADAC8E4D8624318C27DAF1DF040103DF0314F527081CF371DD7E1FD4FA414A665036E0F5E6E5");
		capk.fromTlvList(list);
		capkDao.insert(capk);
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000003339F220108DF050420301231DF060101DF070101DF028190B61645EDFD5498FB246444037A0FA18C0F101EBD8EFA54573CE6E6A7FBF63ED21D66340852B0211CF5EEF6A1CD989F66AF21A8EB19DBD8DBC3706D135363A0D683D046304F5A836BC1BC632821AFE7A2F75DA3C50AC74C545A754562204137169663CFCC0B06E67E2109EBA41BC67FF20CC8AC80D7B6EE1A95465B3B2657533EA56D92D539E5064360EA4850FED2D1BFDF040103DF0314EE23B616C95C02652AD18860E48787C079E8E85A");
		capk.fromTlvList(list);
		capkDao.insert(capk);
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000003339F220109DF05083230333031323331DF060101DF070101DF0281B0EB374DFC5A96B71D2863875EDA2EAFB96B1B439D3ECE0B1826A2672EEEFA7990286776F8BD989A15141A75C384DFC14FEF9243AAB32707659BE9E4797A247C2F0B6D99372F384AF62FE23BC54BCDC57A9ACD1D5585C303F201EF4E8B806AFB809DB1A3DB1CD112AC884F164A67B99C7D6E5A8A6DF1D3CAE6D7ED3D5BE725B2DE4ADE23FA679BF4EB15A93D8A6E29C7FFA1A70DE2E54F593D908A3BF9EBBD760BBFDC8DB8B54497E6C5BE0E4A4DAC29E5DF040103DF0314A075306EAB0045BAF72CDD33B3B678779DE1F527");
		capk.fromTlvList(list);
		capkDao.insert(capk);
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000003339F22010ADF05083230333031323331DF060101DF070101DF028180B2AB1B6E9AC55A75ADFD5BBC34490E53C4C3381F34E60E7FAC21CC2B26DD34462B64A6FAE2495ED1DD383B8138BEA100FF9B7A111817E7B9869A9742B19E5C9DAC56F8B8827F11B05A08ECCF9E8D5E85B0F7CFA644EFF3E9B796688F38E006DEB21E101C01028903A06023AC5AAB8635F8E307A53AC742BDCE6A283F585F48EFDF040103DF0314C88BE6B2417C4F941C9371EA35A377158767E4E3");
		capk.fromTlvList(list);
		capkDao.insert(capk);
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000003339F22010BDF05083230333031323331DF060101DF070101DF0281F8CF9FDF46B356378E9AF311B0F981B21A1F22F250FB11F55C958709E3C7241918293483289EAE688A094C02C344E2999F315A72841F489E24B1BA0056CFAB3B479D0E826452375DCDBB67E97EC2AA66F4601D774FEAEF775ACCC621BFEB65FB0053FC5F392AA5E1D4C41A4DE9FFDFDF1327C4BB874F1F63A599EE3902FE95E729FD78D4234DC7E6CF1ABABAA3F6DB29B7F05D1D901D2E76A606A8CBFFFFECBD918FA2D278BDB43B0434F5D45134BE1C2781D157D501FF43E5F1C470967CD57CE53B64D82974C8275937C5D8502A1252A8A5D6088A259B694F98648D9AF2CB0EFD9D943C69F896D49FA39702162ACB5AF29B90BADE005BC157DF040103DF0314BD331F9996A490B33C13441066A09AD3FEB5F66C");
		capk.fromTlvList(list);
		capkDao.insert(capk);
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000003339F220180DF05083230333031323331DF060101DF070101DF028180CCDBA686E2EFB84CE2EA01209EEB53BEF21AB6D353274FF8391D7035D76E2156CAEDD07510E07DAFCACABB7CCB0950BA2F0A3CEC313C52EE6CD09EF00401A3D6CC5F68CA5FCD0AC6132141FAFD1CFA36A2692D02DDC27EDA4CD5BEA6FF21913B513CE78BF33E6877AA5B605BC69A534F3777CBED6376BA649C72516A7E16AF85DF0403010001DF0314A5E44BB0E1FA4F96A11709186670D0835057D35E");
		capk.fromTlvList(list);
		capkDao.insert(capk);
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000003339F220161DF0803000000DF050420201231DF060101DF070100DF028180834D2A387C5a5f176ef3e66caaf83f194b15aad2470c78c77d6eb38edae3a2f9ba1623f6a58c892cc925632dff48ce954b21a53e1f1e4366be403c279b90027cbc72605db6c79049b8992cb4912efa270becab3a7cefe05bfa46e4c7bbcf7c7a173bd988d989b32cb79fac8e35fbe1860e7ea9f238a92a3593552d03d1e38601df040103df0314008c4830309f455e0ff9eb406ef5f1fe92bfab5e");
		capk.fromTlvList(list);
		capkDao.insert(capk);

		//VISA CAPK
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000000039F220101DF05083230303931323331DF060101DF070101DF028180C696034213D7D8546984579D1D0F0EA519CFF8DEFFC429354CF3A871A6F7183F1228DA5C7470C055387100CB935A712C4E2864DF5D64BA93FE7E63E71F25B1E5F5298575EBE1C63AA617706917911DC2A75AC28B251C7EF40F2365912490B939BCA2124A30A28F54402C34AECA331AB67E1E79B285DD5771B5D9FF79EA630B75DF040103DF0314D34A6A776011C7E7CE3AEC5F03AD2F8CFC5503CC");
		capk.fromTlvList(list);
		capkDao.insert(capk);
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000000039F220107DF05083230313231323331DF060101DF070101DF028190A89F25A56FA6DA258C8CA8B40427D927B4A1EB4D7EA326BBB12F97DED70AE5E4480FC9C5E8A972177110A1CC318D06D2F8F5C4844AC5FA79A4DC470BB11ED635699C17081B90F1B984F12E92C1C529276D8AF8EC7F28492097D8CD5BECEA16FE4088F6CFAB4A1B42328A1B996F9278B0B7E3311CA5EF856C2F888474B83612A82E4E00D0CD4069A6783140433D50725FDF040103DF0314B4BC56CC4E88324932CBC643D6898F6FE593B172");
		capk.fromTlvList(list);
		capkDao.insert(capk);
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000000039F220108DF05083230313431323331DF060101DF070101DF0281B0D9FD6ED75D51D0E30664BD157023EAA1FFA871E4DA65672B863D255E81E137A51DE4F72BCC9E44ACE12127F87E263D3AF9DD9CF35CA4A7B01E907000BA85D24954C2FCA3074825DDD4C0C8F186CB020F683E02F2DEAD3969133F06F7845166ACEB57CA0FC2603445469811D293BFEFBAFAB57631B3DD91E796BF850A25012F1AE38F05AA5C4D6D03B1DC2E568612785938BBC9B3CD3A910C1DA55A5A9218ACE0F7A21287752682F15832A678D6E1ED0BDF040103DF031420D213126955DE205ADC2FD2822BD22DE21CF9A8");
		capk.fromTlvList(list);
		capkDao.insert(capk);
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000000039F220109DF05083230313631323331DF060101DF070101DF0281F89D912248DE0A4E39C1A7DDE3F6D2588992C1A4095AFBD1824D1BA74847F2BC4926D2EFD904B4B54954CD189A54C5D1179654F8F9B0D2AB5F0357EB642FEDA95D3912C6576945FAB897E7062CAA44A4AA06B8FE6E3DBA18AF6AE3738E30429EE9BE03427C9D64F695FA8CAB4BFE376853EA34AD1D76BFCAD15908C077FFE6DC5521ECEF5D278A96E26F57359FFAEDA19434B937F1AD999DC5C41EB11935B44C18100E857F431A4A5A6BB65114F174C2D7B59FDF237D6BB1DD0916E644D709DED56481477C75D95CDD68254615F7740EC07F330AC5D67BCD75BF23D28A140826C026DBDE971A37CD3EF9B8DF644AC385010501EFC6509D7A41DF040103DF03141FF80A40173F52D7D27E0F26A146A1C8CCB29046");
		capk.fromTlvList(list);
		capkDao.insert(capk);


		//MasterCard
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000000049F220103DF05083230303931323331DF060101DF070101DF028180C2490747FE17EB0584C88D47B1602704150ADC88C5B998BD59CE043EDEBF0FFEE3093AC7956AD3B6AD4554C6DE19A178D6DA295BE15D5220645E3C8131666FA4BE5B84FE131EA44B039307638B9E74A8C42564F892A64DF1CB15712B736E3374F1BBB6819371602D8970E97B900793C7C2A89A4A1649A59BE680574DD0B60145DF040103DF03145ADDF21D09278661141179CBEFF272EA384B13BB");
		capk.fromTlvList(list);
		capkDao.insert(capk);
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000000049F220104DF05083230313231323331DF060101DF070101DF028190A6DA428387A502D7DDFB7A74D3F412BE762627197B25435B7A81716A700157DDD06F7CC99D6CA28C2470527E2C03616B9C59217357C2674F583B3BA5C7DCF2838692D023E3562420B4615C439CA97C44DC9A249CFCE7B3BFB22F68228C3AF13329AA4A613CF8DD853502373D62E49AB256D2BC17120E54AEDCED6D96A4287ACC5C04677D4A5A320DB8BEE2F775E5FEC5DF040103DF0314381A035DA58B482EE2AF75F4C3F2CA469BA4AA6C");
		capk.fromTlvList(list);
		capkDao.insert(capk);
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000000049F220105DF05083230313431323331DF060101DF070101DF0281B0B8048ABC30C90D976336543E3FD7091C8FE4800DF820ED55E7E94813ED00555B573FECA3D84AF6131A651D66CFF4284FB13B635EDD0EE40176D8BF04B7FD1C7BACF9AC7327DFAA8AA72D10DB3B8E70B2DDD811CB4196525EA386ACC33C0D9D4575916469C4E4F53E8E1C912CC618CB22DDE7C3568E90022E6BBA770202E4522A2DD623D180E215BD1D1507FE3DC90CA310D27B3EFCCD8F83DE3052CAD1E48938C68D095AAC91B5F37E28BB49EC7ED597DF040103DF0314EBFA0D5D06D8CE702DA3EAE890701D45E274C845");
		capk.fromTlvList(list);
		capkDao.insert(capk);
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000000049F220106DF05083230313631323331DF060101DF070101DF0281F8CB26FC830B43785B2BCE37C81ED334622F9622F4C89AAE641046B2353433883F307FB7C974162DA72F7A4EC75D9D657336865B8D3023D3D645667625C9A07A6B7A137CF0C64198AE38FC238006FB2603F41F4F3BB9DA1347270F2F5D8C606E420958C5F7D50A71DE30142F70DE468889B5E3A08695B938A50FC980393A9CBCE44AD2D64F630BB33AD3F5F5FD495D31F37818C1D94071342E07F1BEC2194F6035BA5DED3936500EB82DFDA6E8AFB655B1EF3D0D7EBF86B66DD9F29F6B1D324FE8B26CE38AB2013DD13F611E7A594D675C4432350EA244CC34F3873CBA06592987A1D7E852ADC22EF5A2EE28132031E48F74037E3B34AB747FDF040103DF0314F910A1504D5FFB793D94F3B500765E1ABCAD72D9");
		capk.fromTlvList(list);
		capkDao.insert(capk);
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000000049F2201F6DF05083230303931323331DF060101DF070101DF0281E0A25A6BD783A5EF6B8FB6F83055C260F5F99EA16678F3B9053E0F6498E82C3F5D1E8C38F13588017E2B12B3D8FF6F50167F46442910729E9E4D1B3739E5067C0AC7A1F4487E35F675BC16E233315165CB142BFDB25E301A632A54A3371EBAB6572DEEBAF370F337F057EE73B4AE46D1A8BC4DA853EC3CC12C8CBC2DA18322D68530C70B22BDAC351DD36068AE321E11ABF264F4D3569BB71214545005558DE26083C735DB776368172FE8C2F5C85E8B5B890CC682911D2DE71FA626B8817FCCC08922B703869F3BAEAC1459D77CD85376BC36182F4238314D6C4212FBDD7F23D3DF040103DF0314502909ED545E3C8DBD00EA582D0617FEE9F6F684");
		capk.fromTlvList(list);
		capkDao.insert(capk);

		//AMEX CAPK
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000000259F22010FDF050420221231DF060101DF070100DF0281B0C8D5AC27A5E1FB89978C7C6479AF993AB3800EB243996FBB2AE26B67B23AC482C4B746005A51AFA7D2D83E894F591A2357B30F85B85627FF15DA12290F70F05766552BA11AD34B7109FA49DE29DCB0109670875A17EA95549E92347B948AA1F045756DE56B707E3863E59A6CBE99C1272EF65FB66CBB4CFF070F36029DD76218B21242645B51CA752AF37E70BE1A84FF31079DC0048E928883EC4FADD497A719385C2BBBEBC5A66AA5E5655D18034EC5df040103df0314A73472B3AB557493A9BC2179CC8014053B12BAB4");
		capk.fromTlvList(list);
		capkDao.insert(capk);
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000000259F2201C9DF050420291231DF060101DF070101DF0281B0B362DB5733C15B8797B8ECEE55CB1A371F760E0BEDD3715BB270424FD4EA26062C38C3F4AAA3732A83D36EA8E9602F6683EECC6BAFF63DD2D49014BDE4D6D603CD744206B05B4BAD0C64C63AB3976B5C8CAAF8539549F5921C0B700D5B0F83C4E7E946068BAAAB5463544DB18C63801118F2182EFCC8A1E85E53C2A7AE839A5C6A3CABE73762B70D170AB64AFC6CA482944902611FB0061E09A67ACB77E493D998A0CCF93D81A4F6C0DC6B7DF22E62DBDF040103DF03148E8DFF443D78CD91DE88821D70C98F0638E51E49");
		capk.fromTlvList(list);
		capkDao.insert(capk);
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000000259F2201CADF050420291231DF060101DF070101DF0281F8C23ECBD7119F479C2EE546C123A585D697A7D10B55C2D28BEF0D299C01DC65420A03FE5227ECDECB8025FBC86EEBC1935298C1753AB849936749719591758C315FA150400789BB14FADD6EAE2AD617DA38163199D1BAD5D3F8F6A7A20AEF420ADFE2404D30B219359C6A4952565CCCA6F11EC5BE564B49B0EA5BF5B3DC8C5C6401208D0029C3957A8C5922CBDE39D3A564C6DEBB6BD2AEF91FC27BB3D3892BEB9646DCE2E1EF8581EFFA712158AAEC541C0BBB4B3E279D7DA54E45A0ACC3570E712C9F7CDF985CFAFD382AE13A3B214A9E8E1E71AB1EA707895112ABC3A97D0FCB0AE2EE5C85492B6CFD54885CDD6337E895CC70FB3255E3DF040103DF03146BDA32B1AA171444C7E8F88075A74FBFE845765F");
		capk.fromTlvList(list);
		capkDao.insert(capk);

		//index: Mada 28
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000002289F220128DF05083230323431323331DF060101DF070101DF0281B0846BA67589E16046557F94C75A5623EC89605F0BEDA8AB0EFE9C321E5423C78C5B2FC08E9ADF558EA4B03DF2FE0B981793D6D3C9F7D493659D98645CD40CCFAA994AAB51A0A1B6228E5873F6BF65BEB172E83A5EBE4C4E145F85CD2B7334BC39F62E512054DD58F0367A173BD2376D75856FBF9D1F2B43463E5460A25186ED7F7C2ABBAFC1F954B2A42D2BA16A38260E5DA05F72283F94DAEAC22CE93DE17D0D76B7B6BF66CAE931466468894DEADFA5DF040103DF03141CCBFF1A83B61EA6FEA65D6E838975DCb700AF4A");
		capk.fromTlvList(list);
		capkDao.insert(capk);

		//index: 92
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000000039F220192DF05083230323831323331DF060101DF070101DF0281B0996AF56F569187D09293C14810450ED8EE3357397B18A2458EFAA92DA3B6DF6514EC060195318FD43BE9B8F0CC669E3F844057CBDDF8BDA191BB64473BC8DC9A730DB8F6B4EDE3924186FFD9B8C7735789C23A36BA0B8AF65372EB57EA5D89E7D14E9C7B6B557460F10885DA16AC923F15AF3758F0F03EBD3C5C2C949CBA306DB44E6A2C076C5F67E281D7EF56785DC4D75945E491F01918800A9E2DC66F60080566CE0DAF8D17EAD46AD8E30A247C9FDF040103DF0314429C954A3859CEF91295F663C963E582ED6EB253");
		capk.fromTlvList(list);
		capkDao.insert(capk);


		//index: 89
		capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000000039F220189DF05083230323831323331DF060101DF070101DF0281C0E5E195705CE61A0672B8367E7A51713927A04289EA308328FAD28071ECEAE889B3C4F29AC3BDE46772B00D42FD05F27228820F2693990F81B0F6928E240D957EC4484354CD5E5CA9092B444741A0394D3476651232474A9B87A961DA8DD96D90F036E9B3C52FB09766BDA4D6BC3BDADBC89122B74068F8FA04026C5FA8EF398BC3AB3992A87F6A785CC779BA99F170956623D67A18EB8324263D626BE85BFF77B8B981C0A3F7849C4F3D8E20542955D19128198547B47AE34DF67F28BE433F33DF040103DF03147170850B97F83952045CF9CA8B7612DFEB69E9EF");
		capk.fromTlvList(list);
		capkDao.insert(capk);
*/


//todo remove comment when TMS is enabled

/*
		Public_Key CAPK[] = TMSManager.getInstance().getAllPublicKeys().toArray(new Public_Key[0]);
	if(CAPK.length>0) {
		for (int i = 0; i < CAPK.length; i++)
			{
				String sCAPK = POS_MAIN.FormatCAKeys(CAPK[i]);
				capk = new Capk();
				list = new TlvList();
				list.fromHex(sCAPK);
				capk.fromTlvList(list);
				capkDao.insert(capk);
			}
		}*/

	}

	public void addTMSCapkAIDtoDB()
	{
		Aid aid = new Aid((byte)0x00);
		TlvList list = new TlvList();
		list.fromHex("9F0607A0000005241010DF0101009F08020140DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400006000DF150400000000DF160199DF170199DF14039F3704DF180100DF1906000000003000DF2006000000010000DF2106000000007500");
		aid.fromTlvList(list);
		aidDao.insert(aid);

		AID_Data AIDdata[] = TMSManager.getInstance().getAidDataList().toArray(new AID_Data[0]);
		if(AIDdata.length>0)
		{
			for (int i = 0; i < AIDdata.length; i++) {
				String sAIDdata = POS_MAIN.FormatAIDData(AIDdata[i]);
				aid = new Aid((byte) 0x00);
				list = new TlvList();
				list.fromHex(sAIDdata);
				aid.fromTlvList(list);
				aidDao.insert(aid);

			}
		}

		Capk capk = new Capk();
		list = new TlvList();
		list.fromHex("9F0605A0000000659F220109DF05083230303931323331DF060101DF070101DF028180B72A8FEF5B27F2B550398FDCC256F714BAD497FF56094B7408328CB626AA6F0E6A9DF8388EB9887BC930170BCC1213E90FC070D52C8DCD0FF9E10FAD36801FE93FC998A721705091F18BC7C98241CADC15A2B9DA7FB963142C0AB640D5D0135E77EBAE95AF1B4FEFADCF9C012366BDDA0455C1564A68810D7127676D493890BDDF040103DF03144410C6D51C2F83ADFD92528FA6E38A32DF048D0A");
		capk.fromTlvList(list);
		capkDao.insert(capk);

		Public_Key CAPK[] = TMSManager.getInstance().getAllPublicKeys().toArray(new Public_Key[0]);
		if(CAPK.length>0) {
			for (int i = 0; i < CAPK.length; i++)
			{
				String sCAPK = POS_MAIN.FormatCAKeys(CAPK[i]);
				capk = new Capk();
				list = new TlvList();
				list.fromHex(sCAPK);
				capk.fromTlvList(list);
				capkDao.insert(capk);
			}
		}
	}
	public void initDaoImpl(Context context){
		setAidDao(new AidDaoImpl(context));
		setCapkDao(new CapkDaoImpl(context));
	}

	public AidDaoImpl getAidDao() {
		return aidDao;
	}

	private void setAidDao(AidDaoImpl aidDao) {
		this.aidDao = aidDao;
	}

	public CapkDaoImpl getCapkDao() {
		return capkDao;
	}

	private void setCapkDao(CapkDaoImpl capkDao) {
		this.capkDao = capkDao;
	}

}
