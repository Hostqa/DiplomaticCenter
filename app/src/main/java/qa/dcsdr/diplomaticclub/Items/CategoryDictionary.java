package qa.dcsdr.diplomaticclub.Items;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import qa.dcsdr.diplomaticclub.R;

/**
 * Created by Tamim on 6/14/2015.
 */
public class CategoryDictionary {
    
    private HashMap dictionary;
    private Context context;

    public CategoryDictionary(Context context) {
        this.context=context;
        this.dictionary = new HashMap<String, String>(43);
        initializeData();
    }

    private String s(int id){
        return context.getString(id);
    }

    @SuppressWarnings("unchecked")
    public List<CategoryEntry> getList(String category) {
        List<CategoryEntry> data = new ArrayList(5);

        if (category.equals(s(R.string.RESEARCH_AND_STUDIES))) {
            CategoryEntry all = new CategoryEntry();
            all.setCategoryImageId(R.drawable.research_studies_cat_compressed);
            all.setCategoryTitle(s(R.string.ALL_RESEARCH_AND_STUDIES));
            List list0 = new ArrayList<String>();
            list0.add(s(R.string.ALL_RESEARCH_AND_STUDIES));
            all.setSubCategories(list0);

            CategoryEntry studies = new CategoryEntry();
            studies.setCategoryImageId(R.drawable.research_studies_cat_compressed);
            studies.setCategoryTitle(s(R.string.STUDIES));
            List list1 = new ArrayList<String>();
            list1.add(s(R.string.POLITICAL_STUDIES));
            list1.add(s(R.string.STRATEGIC_STUDIES));
            list1.add(s(R.string.ECONOMIC_STUDIES));
            list1.add(s(R.string.ENERGY_STUDIES));
            studies.setSubCategories(list1);

            CategoryEntry polAn = new CategoryEntry();
            polAn.setCategoryImageId(R.drawable.research_studies_cat_compressed);
            polAn.setCategoryTitle(s(R.string.POLICIES_ANALYSIS));
            List list2 = new ArrayList<String>();
            list2.add(s(R.string.INTERNAL_POLICY));
            list2.add(s(R.string.FOREIGN_POLICY));
            list2.add(s(R.string.ECONOMIC_POLICY));
            polAn.setSubCategories(list2);

            CategoryEntry posPap = new CategoryEntry();
            posPap.setCategoryImageId(R.drawable.research_studies_cat_compressed);
            posPap.setCategoryTitle(s(R.string.POSITION_PAPERS));
            List list3 = new ArrayList<String>();
            list3.add(s(R.string.POLITICAL_ESTIMATES));
            list3.add(s(R.string.STRATEGIC_ESTIMATES));
            list3.add(s(R.string.ECONOMIC_ESTIMATES));
            posPap.setSubCategories(list3);

            data.add(all);
            data.add(studies);
            data.add(polAn);
            data.add(posPap);

        } else if (category.equals(s(R.string.PUBLICATIONS))) {

            CategoryEntry all = new CategoryEntry();
            all.setCategoryImageId(R.drawable.publications_cat_compressed);
            all.setCategoryTitle(s(R.string.ALL_PUBLICATIONS));
            List list0 = new ArrayList<String>();
            list0.add(s(R.string.ALL_PUBLICATIONS));
            all.setSubCategories(list0);

            CategoryEntry resAndStud = new CategoryEntry();
            resAndStud.setCategoryImageId(R.drawable.publications_cat_compressed);
            resAndStud.setCategoryTitle(s(R.string.RESEARCH_AND_STUDIES_PUBLICATIONS));
            List list1 = new ArrayList<String>();
            list1.add(s(R.string.ARBITRATION_STUDIES));
            list1.add(s(R.string.POLICY_ANALYSIS));
            list1.add(s(R.string.POSITION_ESTIMATES));
            resAndStud.setSubCategories(list1);

            CategoryEntry books = new CategoryEntry();
            books.setCategoryImageId(R.drawable.publications_cat_compressed);
            books.setCategoryTitle(s(R.string.BOOKS));
            List list2 = new ArrayList<String>();
            list2.add(s(R.string.LITERATURE_BOOKS));
            list2.add(s(R.string.TRANSLATED_BOOKS));
            list2.add(s(R.string.EDITOR_BOOKS));
            books.setSubCategories(list2);

            CategoryEntry repAndPer = new CategoryEntry();
            repAndPer.setCategoryImageId(R.drawable.publications_cat_compressed);
            repAndPer.setCategoryTitle(s(R.string.REPORTS_AND_PERIODICALS));
            List list3 = new ArrayList<String>();
            list3.add(s(R.string.MEDIA_REPORTS));
            list3.add(s(R.string.TREND_ANALYSIS_REPORTS));
            list3.add(s(R.string.STRATEGIC_REPORTS));
            repAndPer.setSubCategories(list3);

            data.add(all);
            data.add(resAndStud);
            data.add(books);
            data.add(repAndPer);
        } else if (category.equals(s(R.string.DISPUTES_RESOLUTION))) {
            CategoryEntry all = new CategoryEntry();
            all.setCategoryImageId(R.drawable.disputes_resolution_cat_compressed);
            all.setCategoryTitle(s(R.string.ALL_DISPUTES_RESOLUTION));
            List list0 = new ArrayList<String>();
            list0.add(s(R.string.ALL_DISPUTES_RESOLUTION));
            all.setSubCategories(list0);

            CategoryEntry intNeg = new CategoryEntry();
            intNeg.setCategoryImageId(R.drawable.disputes_resolution_cat_compressed);
            intNeg.setCategoryTitle(s(R.string.INTERNATIONAL_NEGOTIATIONS));
            List list1 = new ArrayList<String>();
            list1.add(s(R.string.PRINCIPLES_OF_NEGOTIATIONS));
            list1.add(s(R.string.ART_OF_DIPLOMACY));
            list1.add(s(R.string.INTERNATIONAL_ARBITRATION));
            list1.add(s(R.string.MEDIATION));
            intNeg.setSubCategories(list1);

            CategoryEntry conAn = new CategoryEntry();
            conAn.setCategoryImageId(R.drawable.disputes_resolution_cat_compressed);
            conAn.setCategoryTitle(s(R.string.CONFLICT_ANALYSIS));
            List list2 = new ArrayList<String>();
            list2.add(s(R.string.ETHNIC_CONFLICTS));
            list2.add(s(R.string.RELIGIOUS_CONFLICTS));
            list2.add(s(R.string.POLITICAL_CONFLICTS));
            list2.add(s(R.string.ECONOMIC_CONFLICTS));
            conAn.setSubCategories(list2);

            CategoryEntry riskReg = new CategoryEntry();
            riskReg.setCategoryImageId(R.drawable.disputes_resolution_cat_compressed);
            riskReg.setCategoryTitle(s(R.string.RISK_REGISTER));
            List list3 = new ArrayList<String>();
            list3.add(s(R.string.SECURITY_RISKS));
            list3.add(s(R.string.ENVIRONMENTAL_RISKS));
            list3.add(s(R.string.POLITICAL_RISKS));
            list3.add(s(R.string.ECONOMIC_RISKS));
            riskReg.setSubCategories(list3);

            CategoryEntry peaceK = new CategoryEntry();
            peaceK.setCategoryImageId(R.drawable.disputes_resolution_cat_compressed);
            peaceK.setCategoryTitle(s(R.string.PEACE_BUILDING_AND_KEEPING));
            List list4 = new ArrayList<String>();
            list4.add(s(R.string.PEACE_KEEPING_CHARTERS));
            list4.add(s(R.string.PEACE_KEEPING));
            list4.add(s(R.string.EXPERIENCES_OF_PEACE_KEEPING));
            list4.add(s(R.string.EXPERIENCES_OF_PEACE_BUILDING));
            peaceK.setSubCategories(list4);

            data.add(all);
            data.add(intNeg);
            data.add(conAn);
            data.add(riskReg);
            data.add(peaceK);

        } else if (category.equals(s(R.string.PROGRAMS_AND_PROJECTS))) {

            CategoryEntry all = new CategoryEntry();
            all.setCategoryImageId(R.drawable.programs_projects_cat_compressed);
            all.setCategoryTitle(s(R.string.ALL_PROGRAMS_AND_PROJECTS));
            List list0 = new ArrayList<String>();
            list0.add(s(R.string.ALL_PROGRAMS_AND_PROJECTS));
            all.setSubCategories(list0);

            CategoryEntry resProj = new CategoryEntry();
            resProj.setCategoryImageId(R.drawable.programs_projects_cat_compressed);
            resProj.setCategoryTitle(s(R.string.RESEARCH_PROJECTS));
            List list1 = new ArrayList<String>();
            if (list1.size()==0)
            list1.add(s(R.string.ALL_RESEARCH_PROJECTS));
            resProj.setSubCategories(list1);

            CategoryEntry resFiles = new CategoryEntry();
            resFiles.setCategoryImageId(R.drawable.programs_projects_cat_compressed);
            resFiles.setCategoryTitle(s(R.string.RESEARCH_FILES));
            List list2 = new ArrayList<String>();
            if (list2.size()==0)
                list2.add(s(R.string.ALL_RESEARCH_FILES));
            resFiles.setSubCategories(list2);

            CategoryEntry leadBuild = new CategoryEntry();
            leadBuild.setCategoryImageId(R.drawable.programs_projects_cat_compressed);
            leadBuild.setCategoryTitle(s(R.string.LEADERSHIP_BUILDING_PROJECTS));
            List list3 = new ArrayList<String>();
            if (list3.size()==0)
                list3.add(s(R.string.ALL_LEADERSHIP_BUILDING_PROJECTS));
            leadBuild.setSubCategories(list3);

            CategoryEntry feasStud = new CategoryEntry();
            feasStud.setCategoryImageId(R.drawable.programs_projects_cat_compressed);
            feasStud.setCategoryTitle(s(R.string.FEASABILITY_STUDIES_PROJECTS));
            List list4 = new ArrayList<String>();
            if (list4.size()==0)
                list4.add(s(R.string.ALL_FEASABILITY_STUDIES));
            feasStud.setSubCategories(list4);

            data.add(all);
            data.add(resProj);
            data.add(resFiles);
            data.add(leadBuild);
            data.add(feasStud);

        } else if (category.equals(s(R.string.EVENTS))) {

            CategoryEntry all = new CategoryEntry();
            all.setCategoryImageId(R.drawable.events_cat_compressed);
            all.setCategoryTitle(s(R.string.ALL_EVENTS));
            List list0 = new ArrayList<String>();
            list0.add(s(R.string.ALL_EVENTS));
            all.setSubCategories(list0);

            CategoryEntry cultSal = new CategoryEntry();
            cultSal.setCategoryImageId(R.drawable.events_cat_compressed);
            cultSal.setCategoryTitle(s(R.string.THE_CULTURAL_SALON));
            List list1 = new ArrayList<String>();
            if (list1.size()==0)
                list1.add(s(R.string.ALL_CULTURAL_SALON));
            cultSal.setSubCategories(list1);

            CategoryEntry dcssNews = new CategoryEntry();
            dcssNews.setCategoryImageId(R.drawable.events_cat_compressed);
            dcssNews.setCategoryTitle(s(R.string.DCSS_NEWS));
            List list2 = new ArrayList<String>();
            if (list2.size()==0)
                list2.add(s(R.string.ALL_DCSS_NEWS));
            dcssNews.setSubCategories(list2);

            CategoryEntry symposiums = new CategoryEntry();
            symposiums.setCategoryImageId(R.drawable.events_cat_compressed);
            symposiums.setCategoryTitle(s(R.string.SYMPOSIUMS));
            List list3 = new ArrayList<String>();
            if (list3.size() == 0)
                list3.add(s(R.string.ALL_SYMPOSIUMS));
            symposiums.setSubCategories(list3);

            CategoryEntry conferences = new CategoryEntry();
            conferences.setCategoryImageId(R.drawable.events_cat_compressed);
            conferences.setCategoryTitle(s(R.string.CONFERENCES));
            List list4 = new ArrayList<String>();
            list4.add(s(R.string.ALL_CONFERENCES));
            conferences.setSubCategories(list4);

            data.add(all);
            data.add(cultSal);
            data.add(dcssNews);
            data.add(symposiums);
            data.add(conferences);
        }
        return data;
    }

    private String urlBuilder(int id, int level) {
        return s(R.string.ID_LEVEL_URL) + id + "&level=" + level;
    }

    @SuppressWarnings("unchecked")
    private void initializeData() {
        // Research & Studies
        // s(R.string.)
        dictionary.put(s(R.string.POLITICAL_STUDIES), urlBuilder(1, 3));
        dictionary.put(s(R.string.STRATEGIC_STUDIES), urlBuilder(2, 3));
        dictionary.put(s(R.string.ECONOMIC_STUDIES), urlBuilder(3, 3));
        dictionary.put(s(R.string.ENERGY_STUDIES), urlBuilder(4, 3));

        dictionary.put(s(R.string.INTERNAL_POLICY), urlBuilder(5, 3));
        dictionary.put(s(R.string.FOREIGN_POLICY), urlBuilder(6, 3));
        dictionary.put(s(R.string.ECONOMIC_POLICY), urlBuilder(7, 3));

                dictionary.put(s(R.string.POLITICAL_ESTIMATES), urlBuilder(8, 3));
        dictionary.put(s(R.string.STRATEGIC_ESTIMATES), urlBuilder(9, 3));
        dictionary.put(s(R.string.ECONOMIC_ESTIMATES), urlBuilder(10, 3));

        // Publications
        dictionary.put(s(R.string.ARBITRATION_STUDIES), urlBuilder(11, 3));
        dictionary.put(s(R.string.POLICY_ANALYSIS), urlBuilder(12, 3));
        dictionary.put(s(R.string.POSITION_ESTIMATES), urlBuilder(13, 3));

        dictionary.put(s(R.string.LITERATURE_BOOKS), urlBuilder(14, 3));
        dictionary.put(s(R.string.TRANSLATED_BOOKS), urlBuilder(15, 3));
        dictionary.put(s(R.string.EDITOR_BOOKS), urlBuilder(16, 3));

        dictionary.put(s(R.string.MEDIA_REPORTS), urlBuilder(17, 3));
        dictionary.put(s(R.string.TREND_ANALYSIS_REPORTS), urlBuilder(18, 3));
        dictionary.put(s(R.string.STRATEGIC_REPORTS), urlBuilder(19, 3));

        //Disputes Resolution
        dictionary.put(s(R.string.PRINCIPLES_OF_NEGOTIATIONS), urlBuilder(20, 3));
        dictionary.put(s(R.string.ART_OF_DIPLOMACY), urlBuilder(21, 3));
        dictionary.put(s(R.string.INTERNATIONAL_ARBITRATION), urlBuilder(22, 3));
        dictionary.put(s(R.string.MEDIATION), urlBuilder(23, 3));

        dictionary.put(s(R.string.ETHNIC_CONFLICTS), urlBuilder(24, 3));
        dictionary.put(s(R.string.RELIGIOUS_CONFLICTS), urlBuilder(25, 3));
        dictionary.put(s(R.string.POLITICAL_CONFLICTS), urlBuilder(26, 3));
        dictionary.put(s(R.string.ECONOMIC_CONFLICTS), urlBuilder(27, 3));

        dictionary.put(s(R.string.SECURITY_RISKS), urlBuilder(28, 3));
        dictionary.put(s(R.string.ENVIRONMENTAL_RISKS), urlBuilder(29, 3));
        dictionary.put(s(R.string.POLITICAL_RISKS), urlBuilder(30, 3));
        dictionary.put(s(R.string.ECONOMIC_RISKS), urlBuilder(31, 3));

        dictionary.put(s(R.string.PEACE_KEEPING_CHARTERS), urlBuilder(32, 3));
        dictionary.put(s(R.string.PEACE_KEEPING), urlBuilder(33, 3));
        dictionary.put(s(R.string.EXPERIENCES_OF_PEACE_KEEPING), urlBuilder(34, 3));
        dictionary.put(s(R.string.EXPERIENCES_OF_PEACE_BUILDING), urlBuilder(35, 3));

        // Programs & Projects
        dictionary.put(s(R.string.ALL_RESEARCH_PROJECTS), urlBuilder(11, 3));
        dictionary.put(s(R.string.ALL_RESEARCH_FILES), urlBuilder(12, 3));
        dictionary.put(s(R.string.ALL_LEADERSHIP_BUILDING_PROJECTS), urlBuilder(13, 3));
        dictionary.put(s(R.string.ALL_FEASABILITY_STUDIES), urlBuilder(14, 3));

        // Events
        dictionary.put(s(R.string.ALL_CULTURAL_SALON), urlBuilder(15, 2));
        dictionary.put(s(R.string.ALL_DCSS_NEWS), urlBuilder(16, 2));
        dictionary.put(s(R.string.ALL_SYMPOSIUMS), urlBuilder(17, 2));
        dictionary.put(s(R.string.ALL_CONFERENCES), urlBuilder(18, 2));

        // All
        dictionary.put(s(R.string.ALL_RESEARCH_AND_STUDIES), urlBuilder(1, 1));
        dictionary.put(s(R.string.ALL_PUBLICATIONS), urlBuilder(2, 1));
        dictionary.put(s(R.string.ALL_DISPUTES_RESOLUTION), urlBuilder(3, 1));
        dictionary.put(s(R.string.ALL_PROGRAMS_AND_PROJECTS), urlBuilder(4, 1));
        dictionary.put(s(R.string.ALL_EVENTS), urlBuilder(5, 1));

    }

    public String getUrl(String subSubCategory) {
        if (dictionary.containsKey(subSubCategory)) {
            return (String) dictionary.get(subSubCategory);
        } else if (dictionary.containsKey(subSubCategory + " All")) {
            return (String) dictionary.get(subSubCategory + " All");
        } else {
            return "";
        }
    }

}
