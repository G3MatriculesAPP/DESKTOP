package sample.models;

import java.util.Date;
import java.util.List;

/**
 * @author alumlocal
 *
 */
public class Modul {

	private String codiModul;
	private String nomModul;
	private int duradaMinModul;
	private int duradaMaxModul;
	private Date dataFiModul;
	private Date dataIniciModul;
	private List<UnitatFormativa> unitatsFormatives;

	public Modul() {}

	public Modul(String codiModul, String nomModul, int duradaMinModul, int duradaMaxModul, Date dataFiModul, Date dataIniciModul) {
		this.codiModul = codiModul;
		this.nomModul = nomModul;
		this.duradaMinModul = duradaMinModul;
		this.duradaMaxModul = duradaMaxModul;
		this.dataFiModul = dataFiModul;
		this.dataIniciModul = dataIniciModul;
	}

	public String getCodiModul() {
		return codiModul;
	}

	public void setCodiModul(String codiModul) {
		this.codiModul = codiModul;
	}

	public String getNomModul() {
		return nomModul;
	}

	public void setNomModul(String nomModul) {
		this.nomModul = nomModul;
	}

	public int getDuradaMinModul() {
		return duradaMinModul;
	}

	public void setDuradaMinModul(int duradaMinModul) {
		this.duradaMinModul = duradaMinModul;
	}

	public int getDuradaMaxModul() {
		return duradaMaxModul;
	}

	public void setDuradaMaxModul(int duradaMaxModul) {
		this.duradaMaxModul = duradaMaxModul;
	}

	public Date getDataFiModul() {
		return dataFiModul;
	}

	public void setDataFiModul(Date dataFiModul) {
		this.dataFiModul = dataFiModul;
	}

	public Date getDataIniciModul() {
		return dataIniciModul;
	}

	public void setDataIniciModul(Date dataIniciModul) {
		this.dataIniciModul = dataIniciModul;
	}

	public List<UnitatFormativa> getUnitatsFormatives() {
		return unitatsFormatives;
	}

	public void setUnitatsFormatives(List<UnitatFormativa> unitatsFormatives) {
		this.unitatsFormatives = unitatsFormatives;
	}

	@Override
	public String toString() {
		return nomModul;
	}

}
