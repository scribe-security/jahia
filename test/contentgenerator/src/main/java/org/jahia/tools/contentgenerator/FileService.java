package org.jahia.tools.contentgenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * Class to handle files used as attachments in Jahia pages
 * 
 * @author Guillaume Lucazeau
 * 
 */
public class FileService {

	String sep;

	public FileService() {
		sep = System.getProperty("file.separator");
	}

	/**
	 * Returns a file name randomly picked in the list of file names available
	 * (from the pool directory specified in parameter)
	 * 
	 * @param availableFileNames
	 * @return file name chosen
	 */
	public String getFileName(List<String> availableFileNames) {
		String fileName = null;

		if (ContentGeneratorService.currentFileIndex == availableFileNames.size()) {
			ContentGeneratorService.currentFileIndex = 0;
		}
		fileName = availableFileNames.get(ContentGeneratorService.currentFileIndex);
		ContentGeneratorService.currentFileIndex++;
		return fileName;
	}

	/**
	 * Returns a list of the files that can be used as attachments Return only
	 * filename as String, sorted alphabetically
	 * 
	 * @param filesDirectoryPath
	 *            directory containing the files that will be uploaded into the
	 *            Jahia repository and can be used as attachments
	 * @return list of file names
	 * @todo: get a list of file actually used as attachment and provide them as
	 *        a zip
	 */
	public List<String> getFileNamesAvailable(File filesDirectory) {
		List<String> fileNames = new ArrayList<String>();
		File[] files = filesDirectory.listFiles();
		Arrays.sort(files);

		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				fileNames.add(files[i].getName());
			}
		}
		return fileNames;
	}

	/**
	 * Returns a list of File available for attachment
	 * 
	 * @param filesDirectory
	 * @return Files available
	 */
	public List<File> getFilesAvailable(File filesDirectory) {
		List<String> filenames = getFileNamesAvailable(filesDirectory);
		List<File> fileList = new ArrayList<File>();
		File f;

		for (Iterator<String> iterator = filenames.iterator(); iterator.hasNext();) {
			String fileName = iterator.next();
			f = new File(filesDirectory + sep + fileName);
			fileList.add(f);
		}
		return fileList;
	}

	/**
	 * 
	 * @param filesToCopy
	 * @param destDir
	 * @returns List of new files (copies)
	 * @throws IOException
	 *             if source or destination is invalid
	 * @throws IOException
	 *             if an IO error occurs during copying
	 */
	public List<File> copyFilesForAttachment(List<File> filesToCopy, File destDir) throws IOException {
		List<File> newFiles = new ArrayList<File>();
		File oldFile;
		File newFile;
		for (Iterator<File> iterator = filesToCopy.iterator(); iterator.hasNext();) {
			oldFile = iterator.next();

			String fileName = oldFile.getName();

			// creates a new directory for each file, with the same name
			// File newDirForFile = new File(destDir + sep + getFileNameWithoutExtension(fileName));
			// newDirForFile.mkdir();

			FileUtils.copyFileToDirectory(oldFile, destDir);
			newFile = new File(destDir + sep + oldFile.getName());
			newFiles.add(newFile);
		}
		return newFiles;
	}

	/**
	 * Creates a file and fill it with XML that we will insert into repository
	 * file, to import files into JCR
	 * 
	 * @param tempXmlFile
	 * @param fileNames
	 * @throws IOException
	 */
	public void createAndPopulateFilesXmlFile(File tempXmlFile, List<File> fileNames) throws IOException {
		GregorianCalendar gc  = (GregorianCalendar) GregorianCalendar.getInstance();
		int year = gc.get(GregorianCalendar.YEAR);
		int month = gc.get(GregorianCalendar.MONTH);
		int day = gc.get(GregorianCalendar.DAY_OF_MONTH);
		int hour = gc.get(GregorianCalendar.HOUR);
		int minute = gc.get(GregorianCalendar.MINUTE);
		int second = gc.get(GregorianCalendar.SECOND);
		
		//String now = year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":" + second;
		String now = "2011-06-09T12:18:35.562-04:00";
		
		FileUtils.writeStringToFile(tempXmlFile, sep);

		StringBuffer filesXml = new StringBuffer();
		filesXml.append("<files j:lastPublished=\"" + now
				+ "\" j:lastPublishedBy=\"root\" j:published=\"true\" jcr:created=\"" + now
				+ "\" jcr:createdBy=\"system\" jcr:lastModified=\"" + now
				+ "\" jcr:lastModifiedBy=\"root\" jcr:primaryType=\"jnt:folder\">");
		filesXml.append("    <contributed j:lastPublished=\"" + now
				+ "\" j:lastPublishedBy=\"root\" j:published=\"true\" jcr:created=\"" + now
				+ "\" jcr:createdBy=\"system\" jcr:lastModified=\"" + now
				+ "\" jcr:lastModifiedBy=\"\" jcr:mixinTypes=\"jmix:accessControlled\" jcr:primaryType=\"jnt:folder\">");
		filesXml.append("     <j:acl jcr:primaryType=\"jnt:acl\">");
		filesXml.append("        <GRANT_g_site-privileged j:aceType=\"GRANT\" j:principal=\"g:privileged\" j:protected=\"false\" j:roles=\"contributor\" jcr:primaryType=\"jnt:ace\" />");
		filesXml.append("     </j:acl>");
		
		for (Iterator<File> iterator = fileNames.iterator(); iterator.hasNext();) {
			File file = iterator.next();
			String fileName = file.getName();
			
			filesXml.append("          <"
					+ fileName
					+ " j:appName=\"" + getFileNameWithoutExtension(fileName) + "\" j:keywords=\"\" j:lastPublished=\""
					+ now
					+ "\" j:lastPublishedBy=\"root\" j:published=\"true\" j:subject=\"\" jcr:created=\""
					+ now
					+ "\" jcr:createdBy=\"root\" jcr:lastModified=\""
					+ now
					+ "\" jcr:lastModifiedBy=\"root\" jcr:mixinTypes=\"jmix:keywords jmix:document\" jcr:primaryType=\"jnt:file\" jcr:title=\"ACME_WP_BuildingFirstClassHotelUS\">");
			filesXml.append("             <jcr:content j:extractedText=\"     Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor  incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud  exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure  dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.  Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit  anim id est laborum.  Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor  incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud  exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure  dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.  Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit  anim id est laborum.  Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor  incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud  exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure  dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.  Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit  anim id est laborum.  Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor  incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud  exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure  dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.  Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit  anim id est laborum.  Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor  incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud  exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure  dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.  Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit  anim id est laborum.   \" j:lastExtractionDate=\""
					+ now
					+ "\" jcr:lastModified=\""
					+ now
					+ "\" jcr:lastModifiedBy=\"\" jcr:mimeType=\"application/pdf\" jcr:primaryType=\"jnt:resource\" />");
			filesXml.append("          </" + fileName + ">");
		}

		filesXml.append("    </contributed>");
		filesXml.append("</files>");


		FileUtils.writeStringToFile(tempXmlFile, filesXml.toString());
	}

	/**
	 * Returns afilename without the extension (removes substring following last
	 * dot)
	 * 
	 * @param fileName
	 * @return filename without extension
	 */
	private String getFileNameWithoutExtension(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf('.'));
	}
}
